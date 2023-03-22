package br.com.petize.aplication.controller;

import br.com.petize.aplication.dto.OrderDto;
import br.com.petize.aplication.service.OrderService;
import br.com.petize.aplication.service.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;
    @PostMapping
    public ResponseEntity<Void> save(@RequestBody @Validated OrderDto orderDto) {
        orderService.save(orderDto);
        return ResponseEntity.ok().build();
    }
    @GetMapping("/{id}")
    public ResponseEntity<OrderDto> findById(@PathVariable("id") Long id) {
        OrderDto order = orderService.findById(id);
        return ResponseEntity.ok(order);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        Optional.ofNullable(orderService.findById(id)).orElseThrow(() -> {
            log.error("Não é possível excluir dados inexistentes!");
            return new ResourceNotFoundException("Não é possível excluir dados inexistentes!");
        });
        orderService.deleteById(id);
        return ResponseEntity.ok().build();
    }



    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@RequestBody @Validated OrderDto orderDto, @PathVariable("id") Long id) {
        orderService.update(orderDto, id);
        return ResponseEntity.ok().build();
    }
}