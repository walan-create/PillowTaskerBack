package org.iesvdm.pillowtaskerback.controller;

import lombok.extern.slf4j.Slf4j;
import org.iesvdm.pillowtaskerback.domain.Client;
import org.iesvdm.pillowtaskerback.service.ClientService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("v1/api/clients")
@Controller
public class ClientController {
    private final ClientService clientService;

    public ClientController(ClientService clientService){
        this.clientService = clientService;
    }

    @GetMapping({"","/"})
    public List<Client> all() {
        log.info("Accediendo a todos los clientes");
        return this.clientService.all();
    }

    @PostMapping({"","/"})
    public Client newCliente(@RequestBody Client client){
        log.info("Creando un client = " + client);
        return this.clientService.save(client);
    }

    @GetMapping("/{id}")
    public Client one(@PathVariable("id") Long id) {
        log.info("Buscar cliente con id: " + id);
        return this.clientService.one(id);
    }

    @PutMapping("/{id}")
    public Client replaceCliente(@PathVariable("id") Long id, @RequestBody Client client) {
        log.info("Actualizar client con id = " + id + "\n client" + client);
        return this.clientService.replace(id, client);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteCliente(@PathVariable ("id") Long id) {
        this.clientService.delete(id);
    }


}

