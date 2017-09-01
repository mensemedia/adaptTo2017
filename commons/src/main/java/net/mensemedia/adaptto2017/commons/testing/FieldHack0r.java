package net.mensemedia.adaptto2017.commons.testing;

import java.lang.reflect.Field;

/**
 * Class FieldHack0r.
 *
 * Useful for tests.
 *
 * <table border="1" summary="Overview of supporter operations">
 *     <tr>
 *     <th>&nbsp;</th>
 *     <th>&nbsp;</th>
 *     <th>(non-final)</th>
 *     <th>final</th>
 *     </tr>
 *
 *     <tr>
 *     <td>public</td>
 *         <td>(non-static)</td>
 *         <td>get + set</td>
 *         <td>get + set</td>
 *     </tr>
 *     <tr>
 *     <td>protected</td>
 *         <td>(non-static)</td>
 *         <td>get + set</td>
 *         <td>get + set</td>
 *     </tr>
 *     <tr>
 *     <td>package local</td>
 *         <td>(non-static)</td>
 *         <td>get + set</td>
 *         <td>get + set</td>
 *     </tr>
 *     <tr>
 *     <td>private</td>
 *         <td>(non-static)</td>
 *         <td>get + set</td>
 *         <td>get + set</td>
 *     </tr>
 *
 *     <tr>
 *     <td>public</td>
 *         <td>static</td>
 *         <td>get + set</td>
 *         <td>get</td>
 *     </tr>
 *     <tr>
 *     <td>protected</td>
 *         <td>static</td>
 *         <td>get + set</td>
 *         <td>get</td>
 *     </tr>
 *     <tr>
 *     <td>package local</td>
 *         <td>static</td>
 *         <td>get + set</td>
 *         <td>get</td>
 *     </tr>
 *     <tr>
 *     <td>private</td>
 *         <td>static</td>
 *         <td>get + set</td>
 *         <td>get</td>
 *     </tr>
 * </table>
 * */
public class FieldHack0r {
    private final Object object;
    private final Class cls;

    public FieldHack0r(final Object object) {
        this.object = object;
        this.cls = object.getClass();
    }

    public <T> T get(final String name) {
        try {
            final Field declaredField = cls.getDeclaredField(name);
            declaredField.setAccessible(true);
            return (T) declaredField.get(object);
        } catch (Exception e) {
            throw new RuntimeReflectionException(e);
        }
    }

    public <T> FieldHack0r set(final String name, final T value) {
        try {
            final Field declaredField = cls.getDeclaredField(name);
            declaredField.setAccessible(true);
            declaredField.set(object, value);
            return this;
        } catch (Exception e) {
            throw new RuntimeReflectionException(e);
        }
    }
}
