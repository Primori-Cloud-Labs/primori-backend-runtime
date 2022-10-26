package primori.backend.runtime;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import primori.base.cadastral.api.adapter.colaborador.provider.ColaboradorAggregateProvider;
import primori.base.cadastral.component.runtime.colaborador.AdministradorAggregateProvider;
import primori.base.cadastral.component.runtime.colaborador.CoordenadorClinicoAggregateProvider;
import primori.base.cadastral.component.runtime.colaborador.CoordenadorTecnicoAggregateProvider;
import primori.base.cadastral.component.runtime.colaborador.FonoAggregateProvider;
import primori.base.cadastral.component.runtime.colaborador.MedicoAggregateProvider;
import primori.base.cadastral.component.runtime.colaborador.TecnicoAggregateProvider;
import primori.base.cadastral.data.colaborador.IColaboradorRepository;
import primori.base.cadastral.adapter.unidade.provider.UnidadeAtendimentoAggregateProvider;
import primori.base.cadastral.component.runtime.unidade.BragancaUnidadeAggregateProvider;
import primori.base.cadastral.data.unidade.IUnidadeAtendimentoRepository;


@SpringBootApplication
@ComponentScan(basePackages = { "primori.base.cadastral.domain", 
		"primori.base.cadastral.component",
		"primori.base.cadastral.data", 
		"primori.base.cadastral.adapter",
		"primori.base.cadastral.api.rest" })
public class RuntimeBoot {
	
	@Autowired
	private IColaboradorRepository repository;
	@Autowired
	private IUnidadeAtendimentoRepository unidadeRepository;


	public static void main(final String[] args) {
		SpringApplication.run(RuntimeBoot.class, args);
	}

	public RuntimeBoot() {}

	@PostConstruct
	public void setUp() {
		configureProvider(AdministradorAggregateProvider.class);
		configureProvider(TecnicoAggregateProvider.class);
		configureProvider(FonoAggregateProvider.class);
		configureProvider(MedicoAggregateProvider.class);
		configureProvider(CoordenadorClinicoAggregateProvider.class);
		configureProvider(CoordenadorTecnicoAggregateProvider.class);
	
		configureUnidadeProvider(BragancaUnidadeAggregateProvider.class, "unidade-bragancaPaulista");
	}
	
	private void configureProvider(Class<? extends ColaboradorAggregateProvider> aggregateClass) {
		this.repository.save ( ColaboradorAggregateProvider.create(aggregateClass) );
	}

	private void configureUnidadeProvider(Class<? extends UnidadeAtendimentoAggregateProvider> aggregateClass,
			String id) {
		this.unidadeRepository.save(UnidadeAtendimentoAggregateProvider.create(aggregateClass, id));
	}

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**").allowedOrigins("http://localhost:3000").allowedMethods("*");
			}
		};
	}

}
