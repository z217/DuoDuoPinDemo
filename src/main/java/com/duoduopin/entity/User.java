package com.duoduopin.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "User")
@Getter
@Setter
public class User {
  @Id
  @Column(name = "id")
  private long id;
  
  @Column(name = "username")
  private String username;
  
  @Column(name = "password")
  private String password;
}
