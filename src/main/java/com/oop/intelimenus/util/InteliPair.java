package com.oop.intelimenus.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class InteliPair<K, V> {

    private K key;
    private V value;

}
