package com.springbatch.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity(name = "person")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long custId;

    private String lastName;
    private String firstName;

    // A method that returns firstName and Lastname when an object of the class is logged
    @Override
    public String toString() {
        return "firstName: " + firstName + ", lastName: " + lastName;
    }
}