package com.abdelrahman.ticketing.entity;

import jakarta.persistence.*;
import lombok.*;

import com.abdelrahman.ticketing.entity.enums.Role;

import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Role role;

    @OneToMany(mappedBy = "createdBy")
    private List<Ticket> createdTickets;

    @OneToMany(mappedBy = "assignedTo")
    private List<Ticket> assignedTickets;

    @OneToMany(mappedBy = "user")
    private List<Comment> comments;

    @OneToMany(mappedBy = "changedBy")
    private List<TicketStatusHistory> statusHistories;
}
