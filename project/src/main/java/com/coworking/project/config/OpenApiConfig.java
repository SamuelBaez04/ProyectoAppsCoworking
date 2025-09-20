import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Gestion de Reservas de Un Coworking",
                version = "1.0.0",
                contact = @Contact(
                        name = "Samuel Valencia Baez, Natalia Sabogal y Thomas Garcia",
                        email = "samuel.valencia.7088@eam.edu.co",
                        url = "eam.edu.co"
                ),
                license = @License(
                        name = "MIT",
                        url = "https://opensource.org/license/mit/"
                )
        ),
        servers = {
                @Server(
                        url = "http://localhost:8080",
                        description = "Servidor de Desarrollo"
                ),
                @Server(
                        url = "api-test.coworking.com",
                        description = "Servidor de Pruebas"
                ),
                @Server(
                        url = "api.coworking.com",
                        description = "Servidor de Produccion"
                )
        }
)
public class OpenApiConfig{

        @Bean
        public OpenAPI customOpenAPI(){
                return new OpenAPI().components(new Components().addSecuritySchemes("Bearer Authentication", new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT").description("Ingrese el token JWT"))
                
                );         
        }

}