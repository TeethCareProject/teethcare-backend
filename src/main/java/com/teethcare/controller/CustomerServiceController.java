package com.teethcare.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@RestController
@EnableSwagger2
@RequestMapping("/api/customer-services")
public class CustomerServiceController {

//    private CRUDService<CustomerService> cSService;
//
//    @Autowired
//    public CustomerServiceController(CRUDService<CustomerService> cSService) {
//        this.cSService = cSService;
//    }
//
//    @GetMapping
//    public ResponseEntity<List<CustomerService>> getAllCustomerServices() {
//        return new ResponseEntity<>(cSService.findAll(), HttpStatus.OK);
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<Optional<CustomerService>> getCustomerService(@PathVariable("id") Integer id) {
//        return new ResponseEntity<>(cSService.findById(id), HttpStatus.OK);
//    }
//
//    @PostMapping
//    public ResponseEntity<CustomerService> addCustomerService(@RequestBody CustomerService customerService) {
//        return new ResponseEntity<>(cSService.save(customerService), HttpStatus.OK);
//
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<CustomerService> delCustomerService(@PathVariable("id") Integer id) {
//        return new ResponseEntity<>(cSService.delete(id), HttpStatus.OK);
//
//    }
}
