package net.hka.common.model.vo;

import javax.persistence.Embeddable;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Address's value object
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@EqualsAndHashCode
@Embeddable
public class SimpleAddress {

    private String city;
    private String street;
    private String zipCode;

    /**
     * Returns a {@link SimpleAddress} given args
     */
    public static SimpleAddress createAddress(String city, String street, String zipCode) {
        return new SimpleAddress(city, street, zipCode);
    }
}
