package com.example.byeongjin_onboarding.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class FormItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "survey_id", nullable = false)
    private Survey survey;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ItemType itemType;

    @Column(nullable = false)
    private boolean required;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "form_item_options", joinColumns = @JoinColumn(name = "form_item_id"))
    @Column(name = "option_value", nullable = false)
    @OrderColumn(name = "option_order")
    private List<String> options = new ArrayList<>();

    @Column(nullable = false)
    private int displayOrder;

    public FormItem(String name, String description, ItemType itemType, boolean required, int displayOrder, List<String> options) {
        this.name = name;
        this.description = description;
        this.itemType = itemType;
        this.required = required;
        this.displayOrder = displayOrder;
        if (options != null) {
            this.options = new ArrayList<>(options);
        } else {
            this.options = new ArrayList<>();
        }
    }
}