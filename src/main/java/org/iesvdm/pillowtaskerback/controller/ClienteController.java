package org.iesvdm.pillowtaskerback.controller;

import lombok.extern.slf4j.Slf4j;
import org.iesvdm.pillowtaskerback.domain.Cliente;
import org.iesvdm.pillowtaskerback.repository.ClienteRepository;
import org.iesvdm.pillowtaskerback.service.ClienteService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/clientes")
@Controller
public class ClienteController {
    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService){
        this.clienteService = clienteService;
    }

    @GetMapping({"","/"})
    public List<Cliente> all() {
        log.info("Accediendo a todos los clientes");
        return this.clienteService.all();
    }

    @PostMapping({"","/"})
    public Cliente newCliente(@RequestBody Cliente cliente){
        log.info("Creando un cliente = " + cliente);
        return this.clienteService.save(cliente);
    }

    @GetMapping("/{id}")
    public Cliente one(@PathVariable("id") Long id) {
        log.info("Buscar cliente con id: " + id);
        return this.clienteService.one(id);
    }

    @PutMapping("/{id}")
    public Cliente replaceCliente(@PathVariable("id") Long id, @RequestBody Cliente cliente) {
        log.info("Actualizar cliente con id = " + id + "\n cliente" + cliente);
        return this.clienteService.replace(id, cliente);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteCliente(@PathVariable ("id") Long id) {
        this.clienteService.delete(id);
    }

}

