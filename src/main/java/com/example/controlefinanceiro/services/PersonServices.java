package com.example.controlefinanceiro.services;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.example.controlefinanceiro.controller.PersonController;
import com.example.controlefinanceiro.data.PersonVO;
import com.example.controlefinanceiro.exceptions.RequiredObjectIsNullException;
import com.example.controlefinanceiro.exceptions.ResourceNotFoundException;
import com.example.controlefinanceiro.mapper.Mapper;
import com.example.controlefinanceiro.model.Person;
import com.example.controlefinanceiro.repository.PersonRepository;


// Classe para pegar as pessoas do banco de dados

@Service
public class PersonServices {

    private Logger logger = Logger.getLogger(PersonServices.class.getName());

    @Autowired
    PersonRepository repository;

    @Autowired
    PagedResourcesAssembler<PersonVO> assembler;

    // Método para paginar e encontrar pessoas
    public PagedModel<EntityModel<PersonVO>> findAll(Pageable pageable){
        logger.info("Mostrar todas as pessoas");
        
        var personPage = repository.findAll(pageable);

        var personVosPage = personPage.map(p -> Mapper.parseObject(p, PersonVO.class));

        personVosPage.map(p -> p.add(linkTo(methodOn(PersonController.class).
        findById(p.getKey())).withSelfRel()));

        Link link = linkTo(methodOn(PersonController.class).findAll(pageable.getPageNumber()
            , pageable.getPageSize(), "asc")).withSelfRel();

        return assembler.toModel(personVosPage, link);
    }

    public PersonVO findById(Long id){
        logger.info("Procurar uma pessoa pelo id");

        var entity = repository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("ID não encontrado"));

        PersonVO vo = Mapper.parseObject(entity, PersonVO.class);
        vo.add(linkTo(methodOn(PersonController.class).findById(id)).withSelfRel());
        return vo;
    }

    /*public PagedModel<EntityModel<PersonVO>> findPeopleByName(String firstname, Pageable pageable){
        logger.info("Procurar pessoa pelo nome");
        
        var personPage = repository.findPeopleByName(firstname, pageable);

        var personVosPage = personPage.map(p -> Mapper.parseObject(p, PersonVO.class));

        personVosPage.map(p -> p.add(linkTo(methodOn(PersonController.class).
        findById(p.getKey())).withSelfRel()));

        Link link = linkTo(methodOn(PersonController.class).findAll(pageable.getPageNumber()
            , pageable.getPageSize(), "asc")).withSelfRel();

        return assembler.toModel(personVosPage, link);
    }*/

    public PersonVO create(PersonVO person){
        if(person == null) throw new RequiredObjectIsNullException();

        logger.info("Creating one person");
        var entity = Mapper.parseObject(person, Person.class);
        var vo = Mapper.parseObject(repository.save(entity), PersonVO.class);
        vo.add(linkTo(methodOn(PersonController.class).findById(vo.getKey())).withSelfRel());
        return vo;
    }

    public PersonVO update(PersonVO person){
        if(person == null) throw new RequiredObjectIsNullException();
        
        logger.info("Updating one people");

        // Procura a pessoa pelo Id e após achar coloca em um atributo
        // para poder mudar os atributos
        var entity = repository.findById(person.getKey())
                        .orElseThrow(() -> new ResourceNotFoundException("ID não encontrado"));

        entity.setFirstName(person.getFirstName());
        entity.setLastName(person.getLastName());
        entity.setSaldo(person.getSaldo());

        var vo = Mapper.parseObject(repository.save(entity), PersonVO.class);
        vo.add(linkTo(methodOn(PersonController.class).findById(vo.getKey())).withSelfRel());
        return vo;
    }

    public void delete(Long id){
        logger.info("Deletando usuário pelo Id");

        var entity = repository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("ID não encontrado"));

        repository.delete(entity);
    }
    
}