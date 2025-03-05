package org.iesvdm.pillowtaskerback;

import org.iesvdm.pillowtaskerback.domain.Empleado;
import org.iesvdm.pillowtaskerback.enums.TipoEmpleadoEnum;
import org.iesvdm.pillowtaskerback.domain.Usuario;
import org.iesvdm.pillowtaskerback.domain.Hotel;
import org.iesvdm.pillowtaskerback.repository.EmpleadoRepository;
import org.iesvdm.pillowtaskerback.repository.HotelRepository;
import org.iesvdm.pillowtaskerback.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest // Carga solo la capa de persistencia y usa una BD en memoria (H2)
class EmpleadoRepositoryTest {

    @Autowired
    private EmpleadoRepository empleadoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private HotelRepository hotelRepository;

    @Test
    @Commit
    void testGuardarYRecuperarUsuariosYEmpleados() {

        // Crear Dueño del Hotel
        Usuario owner = Usuario.builder()
                .nombre("Carlos Dueño")
                .email("carlos.dueno@example.com")
                .build();
        owner = usuarioRepository.save(owner);

        // Crear Hotel con el dueño
        Hotel hotel = Hotel.builder()
                .nombre("Hotel Paraíso")
                .direccion("Calle Principal 123, Madrid")
                .codigoPostal("29645")
                .owner(owner) // Asignamos el usuario dueño del hotel
                .build();
        hotel = hotelRepository.save(hotel);

        // Crear Empleado 1
        Usuario usuario1 = Usuario.builder()
                .nombre("xX_JuanDestroyer_Xx")
                .email("juan.perez@example.com")
                .build();
        usuario1 = usuarioRepository.save(usuario1);

        Empleado empleado1 = Empleado.builder()
                .nombre("Juan")
                .Apellido1("Pérez")
                .Apellido2("Gómez")
                .contrasenia("password123")
                .tipo(TipoEmpleadoEnum.RECEPCIONISTA)
                .usuario(usuario1) //Asignamos el usuario al que corresponde este empleado
                .hotel(hotel)
                .build();
        empleadoRepository.save(empleado1);

        // Crear Empleado 2
        Usuario usuario2 = Usuario.builder()
                .nombre("Anita")
                .email("ana.lopez@example.com")
                .build();
        usuario2 = usuarioRepository.save(usuario2);
        
        Empleado empleado2 = Empleado.builder()
                .nombre("Ana")
                .Apellido1("López")
                .Apellido2("Martínez")
                .contrasenia("securePass456")
                .tipo(TipoEmpleadoEnum.LIMPIEZA)
                .usuario(usuario2) //Asignamos el usuario al que corresponde este empleado
                .hotel(hotel)
                .build();
        empleadoRepository.save(empleado2);

        // Verificar que los empleados se guardaron correctamente
        assertThat(empleadoRepository.count()).isEqualTo(2);
        assertThat(usuarioRepository.count()).isEqualTo(3); // Dueño + 2 empleados
        assertThat(hotelRepository.count()).isEqualTo(1);
    }
}
