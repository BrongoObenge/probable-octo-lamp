package nl.hr.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by j on 6/20/16.
 */

@Data
@AllArgsConstructor
public class TwoDPoints<T> {
    private T x;
    private T y;
}
