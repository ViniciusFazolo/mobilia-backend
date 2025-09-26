package com.example.mobilia.services;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import com.example.mobilia.domain.Contrato;
import com.example.mobilia.domain.Imovel;
import com.example.mobilia.domain.Morador;
import com.example.mobilia.domain.Unidade;
import com.example.mobilia.domain.User;
import com.example.mobilia.dto.contrato.ContratoRequestDTO;
import com.example.mobilia.dto.contrato.ContratoResponseDTO;
import com.example.mobilia.mapper.ContratoMapper;
import com.example.mobilia.repository.ContratoRepository;
import com.example.mobilia.repository.ImovelRepository;
import com.example.mobilia.repository.MoradorRepository;
import com.example.mobilia.repository.UnidadeRepository;
import com.example.mobilia.repository.UserRepository;
import com.itextpdf.html2pdf.HtmlConverter;

@Service
public class ContratoService extends CrudServiceImpl<Contrato, ContratoRequestDTO, ContratoResponseDTO, Long> {
    
    private final ContratoRepository contratoRepository;
    private final MoradorRepository moradorRepository;
    private final UnidadeRepository unidadeRepository;
    private final ImovelRepository imovelRepository;
    private final UserRepository userRepository;
    private final ContratoMapper contratoMapper;
    private final SpringTemplateEngine templateEngine;
    private final CloudinaryService cloudinaryService;
    

    public ContratoService(ContratoRepository repository, MoradorRepository moradorRepository, UnidadeRepository unidadeRepository, ImovelRepository imovelRepository, ContratoMapper contratoMapper, SpringTemplateEngine templateEngine, UserRepository userRepository, CloudinaryService cloudinaryService) {
        super(repository, contratoMapper);
        this.contratoRepository = repository;
        this.moradorRepository = moradorRepository;
        this.unidadeRepository = unidadeRepository;
        this.imovelRepository = imovelRepository;
        this.contratoMapper = contratoMapper;
        this.templateEngine = templateEngine;
        this.userRepository = userRepository;
        this.cloudinaryService = cloudinaryService;
    }
    
    @Override
    public ContratoResponseDTO create(ContratoRequestDTO dto) {
        Morador morador = moradorRepository.findById(dto.morador())
            .orElseThrow(() -> new RuntimeException("Morador não encontrado"));
        
        Unidade unidade = unidadeRepository.findById(dto.unidade())
            .orElseThrow(() -> new RuntimeException("Unidade não encontrada"));
        
        Imovel imovel = imovelRepository.findById(dto.imovel())
            .orElseThrow(() -> new RuntimeException("Imóvel não encontrado"));

        User user = userRepository.findById(dto.user())
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        
        Contrato contrato = contratoMapper.toEntity(dto);
        contrato.setMorador(morador);
        contrato.setUnidade(unidade);
        contrato.setImovel(imovel);
        contrato.setLocador(user);
        contrato = contratoRepository.save(contrato);
        
        // Gerar PDF
        String caminhoPdf = gerarEUploadPdfContrato(contrato);
        contrato.setPdfContrato(caminhoPdf);
        contrato = contratoRepository.save(contrato);
        
        return contratoMapper.toDto(contrato);
    }
    
    @Override
    public ContratoResponseDTO update(Long id, ContratoRequestDTO dto) {
        Contrato contrato = contratoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Contrato não encontrado"));
        
        // Atualizar dados básicos
        contrato.setDataInicio(dto.dataInicio());
        contrato.setDataFim(dto.dataFim());
        contrato.setDataVencimento(dto.dataVencimento());
        contrato.setValorAluguel(dto.valorAluguel());
        contrato.setValorDeposito(dto.valorDeposito());
        
        // Atualizar dados do locatário se fornecidos
        if (dto.user() != null) {
            contrato.setLocador(userRepository.findById(dto.user())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado")));
        }

        // Atualizar dados do locador se fornecidos
        if (dto.morador() != null) {
            contrato.setMorador(moradorRepository.findById(dto.morador())
                .orElseThrow(() -> new RuntimeException("Morador não encontrado")));
        }
        
        // Regenerar PDF se necessário
        String caminhoPdf = gerarEUploadPdfContrato(contrato);
        contrato.setPdfContrato(caminhoPdf);

        contrato = contratoRepository.save(contrato);
        return contratoMapper.toDto(contrato);
    }

    private String gerarEUploadPdfContrato(Contrato contrato) {
        try {
            // Preparar contexto para o template
            Context context = new Context();
            context.setVariable("contrato", contrato);
            context.setVariable("imovel", contrato.getImovel());
            context.setVariable("unidade", contrato.getUnidade());
            context.setVariable("morador", contrato.getMorador());
            context.setVariable("locador", contrato.getLocador());
            context.setVariable("valorExtenso", converter(contrato.getValorAluguel()));
            Locale localeBR = Locale.forLanguageTag("pt-BR");   
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd 'de' MMMM 'de' yyyy", localeBR);
            context.setVariable("dataContratoFormatada", contrato.getDataInicio().format(formatter).toUpperCase());
            
            // Processar template HTML
            String html = templateEngine.process("contrato", context);
            
            // Converter HTML para PDF
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            HtmlConverter.convertToPdf(html, outputStream);
            
            // Criar MultipartFile a partir do ByteArrayOutputStream para upload
            String nomeArquivo = "contrato_" + contrato.getId() + "_" + 
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".pdf";
            
            String urlCloudinary = cloudinaryService.uploadPdfFile(outputStream.toByteArray(), nomeArquivo);

            return urlCloudinary;
            
        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar PDF do contrato: " + e.getMessage(), e);
        }
    }

    public byte[] downloadPdf(Long id) {
        Contrato contrato = contratoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Contrato não encontrado"));
        
        if (contrato.getPdfContrato() == null) {
            throw new RuntimeException("PDF do contrato não foi gerado");
        }
        
        try {
            return cloudinaryService.downloadPdfFile(contrato.getPdfContrato());
        } catch (Exception e) {
            throw new RuntimeException("Erro ao ler arquivo PDF: " + e.getMessage());
        }
    }
    
    @Override
    public void delete(Long id) {
        Contrato contrato = contratoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Contrato não encontrado"));
        
        if (contrato.getPdfContrato() != null && !contrato.getPdfContrato().isEmpty()) {
            try {
                cloudinaryService.deleteFileByUrl(contrato.getPdfContrato());
            } catch (Exception e) {
                System.err.println("Erro ao excluir PDF do Cloudinary: " + e.getMessage());
            }
        }
        
        contratoRepository.deleteById(id);
    }
    
    public String converter(double valor) {
        long parteInteira = (long) valor;
        int centavos = (int) Math.round((valor - parteInteira) * 100);
    
        String extenso = converterNumeroParaExtenso(parteInteira);
    
        if (parteInteira == 1) {
            extenso += " real";
        } else {
            extenso += " reais";
        }
    
        if (centavos > 0) {
            extenso += " e " + converterNumeroParaExtenso(centavos);
            if (centavos == 1) {
                extenso += " centavo";
            } else {
                extenso += " centavos";
            }
        }
    
        return extenso;
    }
    
    private String converterNumeroParaExtenso(long numero) {
        if (numero == 0) return "zero";
    
        String[] unidades = {"", "um", "dois", "três", "quatro", "cinco", "seis", "sete", "oito", "nove"};
        String[] dezenas = {"", "", "vinte", "trinta", "quarenta", "cinquenta", "sessenta", "setenta", "oitenta", "noventa"};
        String[] especiais = {"dez", "onze", "doze", "treze", "quatorze", "quinze", "dezesseis", "dezessete", "dezoito", "dezenove"};
        String[] centenas = {"", "cento", "duzentos", "trezentos", "quatrocentos", "quinhentos", "seiscentos", "setecentos", "oitocentos", "novecentos"};
    
        if (numero < 10) {
            return unidades[(int) numero];
        } else if (numero < 20) {
            return especiais[(int) numero - 10];
        } else if (numero < 100) {
            int dez = (int) numero / 10;
            int uni = (int) numero % 10;
            return dezenas[dez] + (uni > 0 ? " e " + unidades[uni] : "");
        } else if (numero == 100) {
            return "cem";
        } else if (numero < 1000) {
            int cen = (int) numero / 100;
            int resto = (int) numero % 100;
            return centenas[cen] + (resto > 0 ? " e " + converterNumeroParaExtenso(resto) : "");
        } else if (numero < 1000000) {
            int mil = (int) numero / 1000;
            int resto = (int) numero % 1000;
            String milExtenso = mil == 1 ? "mil" : converterNumeroParaExtenso(mil) + " mil";
            return milExtenso + (resto > 0 ? " e " + converterNumeroParaExtenso(resto) : "");
        }
    
        return String.valueOf(numero);
    }
}
