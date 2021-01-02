package com.example.pamw.controller;

import com.example.pamw.entity.Parcel;
import com.example.pamw.entity.ParcelStatusEnum;
import com.example.pamw.entity.User;
import com.example.pamw.payload.request.ParcelRequest;
import com.example.pamw.payload.request.UpdateParcelStatusRequest;
import com.example.pamw.repository.ParcelRepository;
import com.example.pamw.repository.UserRepository;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/parcel")
public class ParcelController {

    private final UserRepository userRepository;
    private final ParcelRepository parcelRepository;

    public ParcelController(UserRepository userRepository, ParcelRepository parcelRepository) {
        this.userRepository = userRepository;
        this.parcelRepository = parcelRepository;
    }

    @GetMapping(path = "/getAllByAuthenticated", produces = "application/hal+json")
    @ResponseBody
    public ResponseEntity<?> getAllByAuthenticated(Authentication authentication) {
        System.out.println(authentication.getAuthorities());
        String username = authentication.getName();
        User sender = userRepository.findByUsername(username).get();
        List<EntityModel<Parcel>> employees = StreamSupport.stream(parcelRepository.findAllBySender(sender).spliterator(), false)
                .map(employee -> EntityModel.of(employee,
                        linkTo(methodOn(ParcelController.class).getOne(authentication, employee.getId())).withSelfRel(),
                        linkTo(methodOn(ParcelController.class).getAllByAuthenticated(authentication)).withRel("getAllByAuthenticated")))
                .collect(Collectors.toList());

        return ResponseEntity.ok(
                CollectionModel.of(employees,
                        linkTo(methodOn(ParcelController.class).getAllByAuthenticated(authentication)).withSelfRel()));
    }

    //@PreAuthorize("hasAuthority('COURIER')")
    @GetMapping(path = "/", produces = "application/hal+json")
    @ResponseBody
    public ResponseEntity<?> getAll(Authentication authentication) {
        List<EntityModel<Parcel>> employees = StreamSupport.stream(parcelRepository.findAll().spliterator(), false)
                .map(employee -> EntityModel.of(employee,
                        linkTo(methodOn(ParcelController.class).getOne(authentication, employee.getId())).withSelfRel(),
                        linkTo(methodOn(ParcelController.class).getAll(authentication)).withRel("getAllByAuthenticated")))
                .collect(Collectors.toList());

        return ResponseEntity.ok(
                CollectionModel.of(employees,
                        linkTo(methodOn(ParcelController.class).getAll(authentication)).withSelfRel()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteParcel(Authentication authentication, @PathVariable String id) {
        String username = authentication.getName();
        Parcel parcel = parcelRepository.findById(id).get();
        if (!parcel.getSenderName().equals(username)) {
            return ResponseEntity.badRequest().body("Parcel does not belong to authenticated user.");
        }
        if (!parcel.getStatus().equals(ParcelStatusEnum.CREATED)) {
            return ResponseEntity.badRequest().body("Cannot delete parcel with status other than CREATED");
        }
        parcelRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping(path = "/{id}", produces = "application/hal+json")
    @ResponseBody
    public ResponseEntity<?> getOne(Authentication authentication, @PathVariable String id) {
        return parcelRepository.findById(id)
                .map(parcel -> EntityModel.of(parcel,
                        linkTo(methodOn(ParcelController.class).getOne(authentication, parcel.getId())).withSelfRel(),
                        linkTo(methodOn(ParcelController.class).getAllByAuthenticated(authentication)).withRel("allParcels")))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping(path = "/", produces = "application/hal+json")
    @ResponseBody
    public ResponseEntity<?> addParcel(Authentication authentication, @RequestBody ParcelRequest parcelRequest) {
        String username = authentication.getName();
        User sender = userRepository.findByUsername(username).get();
        Parcel parcel = new Parcel(parcelRequest.getReceiver(), parcelRequest.getPostOffice(), parcelRequest.getSize(), sender);
        try {
            Parcel savedParcel = parcelRepository.save(parcel);

            EntityModel<Parcel> employeeResource = EntityModel.of(savedParcel,
                    linkTo(methodOn(ParcelController.class).getOne(authentication, savedParcel.getId())).withSelfRel());

            return ResponseEntity
                    .created(new URI(employeeResource.getRequiredLink(IanaLinkRelations.SELF).getHref()))
                    .body(employeeResource);
        } catch (URISyntaxException e) {
            return ResponseEntity.badRequest().body("Unable to create " + parcel);
        }
    }

    //@PreAuthorize("hasAuthority('COURIER')")
    @PatchMapping(path = "/{id}", produces = "application/hal+json")
    @ResponseBody
    public ResponseEntity<?> updateParcelStatus(Authentication authentication, @RequestBody UpdateParcelStatusRequest request, @PathVariable String id) {
        ParcelStatusEnum statusEnum;
        try {
            statusEnum = ParcelStatusEnum.valueOf(request.getStatus());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid status");
        }
        Parcel parcel = parcelRepository.findById(id).get();
        parcel.setStatus(statusEnum);
        try {
            Parcel savedParcel = parcelRepository.save(parcel);
            EntityModel<Parcel> employeeResource = EntityModel.of(savedParcel,
                    linkTo(methodOn(ParcelController.class).getOne(authentication, savedParcel.getId())).withSelfRel());

            return ResponseEntity
                    .created(new URI(employeeResource.getRequiredLink(IanaLinkRelations.SELF).getHref()))
                    .body(employeeResource);
        } catch (URISyntaxException e) {
            return ResponseEntity.badRequest().body("Unable to update " + parcel);
        }

    }
}
