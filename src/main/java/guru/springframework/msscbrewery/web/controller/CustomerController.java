package guru.springframework.msscbrewery.web.controller;

import guru.springframework.msscbrewery.services.CustomerService;
import guru.springframework.msscbrewery.web.model.CustomerDto;
import org.apache.coyote.Response;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpResponse;
import java.util.UUID;

@RequestMapping("/api/v1/customer")
@RestController
public class CustomerController
{
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<CustomerDto> getCustomer(@PathVariable("customerId") UUID customerId){
        return new ResponseEntity<>(customerService.getCustomerById(customerId), HttpStatus.OK);
    }

    @PostMapping// POST - create new customer
    public ResponseEntity handlePost(@RequestBody CustomerDto customerDto) {
        CustomerDto saveDto = customerService.saveNewCustomer(customerDto);
        HttpHeaders headers = new HttpHeaders();
        //todo add hostname to URL
        headers.add("Location", "/api/v1/customer" + saveDto.getId().toString());
        return new ResponseEntity(headers, HttpStatus.CREATED);
    }

    @PutMapping({"/{customerId}"})
    public ResponseEntity handleUpdate(@PathVariable("customerId") UUID customerId, @RequestBody CustomerDto customerDto) {
        customerService.updateCustomer(customerId, customerDto);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping({"/{customerId}"})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCustomer(@PathVariable("customerId") UUID customerId){
        customerService.deleteById(customerId);
    }

}
