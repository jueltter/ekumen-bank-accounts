package ec.dev.samagua.ekumen_bank_accounts.utils;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.PropertyDescriptor;
import java.util.HashSet;
import java.util.Set;

public class BeanCopyUtil {
    public static void copyNonNullProperties(Object source, Object target) {
        BeanUtils.copyProperties(source, target, getNullPropertyNames(source));
    }

    private static String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> nullProps = new HashSet<>();
        for (PropertyDescriptor pd : pds) {
            Object value = src.getPropertyValue(pd.getName());
            if (value == null) {
                nullProps.add(pd.getName());
            }
        }

        return nullProps.toArray(new String[0]);
    }
}
