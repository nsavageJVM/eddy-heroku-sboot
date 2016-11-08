package com.eduonix;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by Rajdeep Dua on 14/3/16.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ticket {
    private int id;
    private String title;
    private String content;


}
