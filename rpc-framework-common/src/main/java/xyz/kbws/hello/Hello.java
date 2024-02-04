package xyz.kbws.hello;

import lombok.*;

import java.io.Serializable;

/**
 * @author kbws
 * @date 2024/1/21
 * @description:
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class Hello implements Serializable {
    private String message;
    private String description;
}
