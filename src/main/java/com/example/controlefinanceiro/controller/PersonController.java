package com.example.controlefinanceiro.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.controlefinanceiro.data.PersonVO;
import com.example.controlefinanceiro.services.PersonServices;
import com.example.controlefinanceiro.util.MediaType;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("api/person/v1")
@Tag(name = "Controle Financeiro", description = "Endpoints para manipular dados das pessoas")
public class PersonController {

    @Autowired
    private PersonServices service;

    @GetMapping(produces = {MediaType.APPLICATION_JSON , MediaType.APPLICATION_XML ,  MediaType.APPLICATION_YAML})

    @Operation(summary = "Listar todas as pessoas", description = "Listar todas as pessoas",
        tags = {"Controle Financeiro"},
        responses = {
                @ApiResponse(description = "Success", responseCode = "200",
                        content = { 
                                @Content(
                                        mediaType = "application/json",
                                        array = @ArraySchema(schema = @Schema(implementation = PersonVO.class))
                                )
                                        
                        }),
                @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content), 
                @ApiResponse(description = "Not Found", responseCode = "404", content = @Content), 
                @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content), 
                @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content)       
        }   
    )
    public ResponseEntity<PagedModel<EntityModel<PersonVO>>> findAll(
        @RequestParam(value = "page", defaultValue = "0") Integer page,
        @RequestParam(value = "limit", defaultValue = "12") Integer limit,
        @RequestParam(value = "direction", defaultValue = "asc") String direction
    )
    {

        var sortDirection = "desc".equalsIgnoreCase(direction) 
                        ? Direction.DESC : Direction.ASC;

        Pageable pageable = PageRequest.of(page, limit, Sort.by(sortDirection,"firstName"));
        return ResponseEntity.ok(service.findAll(pageable));
    }

    @GetMapping(value = "/findPeopleByName/{firstName}",
        produces = {MediaType.APPLICATION_JSON , MediaType.APPLICATION_XML ,  MediaType.APPLICATION_YAML})

    @Operation(summary = "Listar pessoas pelo nome", description = "Listar pessoas pelo nome",
        tags = {"Controle Financeiro"},
        responses = {
                @ApiResponse(description = "Success", responseCode = "200",
                        content = { 
                                @Content(
                                        mediaType = "application/json",
                                        array = @ArraySchema(schema = @Schema(implementation = PersonVO.class))
                                )
                                        
                        }),
                @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content), 
                @ApiResponse(description = "Not Found", responseCode = "404", content = @Content), 
                @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content), 
                @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content)       
        }   
    )
    public ResponseEntity<PagedModel<EntityModel<PersonVO>>> findPeopleByName(
        @PathVariable(value = "firstName") String firstName,
        @RequestParam(value = "page", defaultValue = "0") Integer page,
        @RequestParam(value = "limit", defaultValue = "12") Integer limit,
        @RequestParam(value = "direction", defaultValue = "asc") String direction
    )
    {

        var sortDirection = "desc".equalsIgnoreCase(direction) 
                        ? Direction.DESC : Direction.ASC;

        Pageable pageable = PageRequest.of(page, limit, Sort.by(sortDirection,"firstName"));
        return ResponseEntity.ok(service.findPeopleByName(firstName,pageable));
    }


    // Permite acesso apenas pelo localhosta da porta 8080
    @CrossOrigin(origins = "http://localhost:8080")
    // Entre chaves é necessário passar parâmetros obrigatórios
    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON , MediaType.APPLICATION_XML , MediaType.APPLICATION_YAML})
    @Operation(summary = "Listar uma pessoa pelo id", description = "Listar uma pessoa pelo id",
        tags = {"Controle Financeiro"},
        responses = {
                @ApiResponse(description = "Success", responseCode = "200",
                        content = @Content(schema = @Schema(implementation = PersonVO.class))
                ),
                @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
                @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content), 
                @ApiResponse(description = "Not Found", responseCode = "404", content = @Content), 
                @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content), 
                @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content)       
        }
    )   
    public PersonVO findById(@PathVariable(value = "id") Long id){
            return service.findById(id);
    }

    @CrossOrigin(origins = {"http://localhost:8080", "https://sergio.com.br"})
    @PostMapping(consumes = {MediaType.APPLICATION_JSON , MediaType.APPLICATION_XML , MediaType.APPLICATION_YAML},
                produces = {MediaType.APPLICATION_JSON , MediaType.APPLICATION_XML ,  MediaType.APPLICATION_YAML})
    @Operation(summary = "Adcionar uma pessoa", description = "Adicionar uma pessoa passando JSON, XML ou YAML",
        tags = {"Controle Financeiro"},
        responses = {
                @ApiResponse(description = "Created", responseCode = "200",
                        content = @Content(schema = @Schema(implementation = PersonVO.class))
                ),
                @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content), 
                @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content), 
                @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content)       
        }
    ) 
    public PersonVO create(@RequestBody PersonVO PersonVO){
            return service.create(PersonVO);
    }

    @PutMapping(consumes = {MediaType.APPLICATION_JSON , MediaType.APPLICATION_XML , MediaType.APPLICATION_YAML},
                produces = {MediaType.APPLICATION_JSON , MediaType.APPLICATION_XML , MediaType.APPLICATION_YAML})
    @Operation(summary = "Atualizar uma informação da pessoa pelo id", description = "Atualizar uma informação da pessoa pelo id",
        tags = {"Controle Financeiro"},
        responses = {
                @ApiResponse(description = "Updated", responseCode = "200",
                        content = @Content(schema = @Schema(implementation = PersonVO.class))
                ),
                @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content), 
                @ApiResponse(description = "Not Found", responseCode = "404", content = @Content), 
                @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content), 
                @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content)       
        }
    ) 
    public PersonVO update(@RequestBody PersonVO PersonVO){
            return service.update(PersonVO);
    }

    @DeleteMapping(value = "/{id}")
    @Operation(summary = "Apagar uma pessoa pelo id", description = "Apagar uma pessoa pelo id",
        tags = {"Controle Financeiro"},
        responses = {
                @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
                @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content), 
                @ApiResponse(description = "Not Found", responseCode = "404", content = @Content), 
                @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content), 
                @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content)       
        }
    ) 
    public ResponseEntity<?> delete(@PathVariable(value = "id") Long id){
            service.delete(id);

            // Retorna o status code correto, nesse caso o status 204
            return ResponseEntity.noContent().build();
    }
}
