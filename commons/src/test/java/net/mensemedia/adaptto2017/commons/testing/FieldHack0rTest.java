package net.mensemedia.adaptto2017.commons.testing;
import net.mensemedia.adaptto2017.commons.testing.p2.PublicClass;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class FieldHack0rTest {
    PublicClass publicClass;
    FieldHack0r fieldHack0r;

    @Before
    public void setUp() throws Exception {
        publicClass = new PublicClass();
        fieldHack0r = new FieldHack0r(publicClass);
    }


    void assertGetAndSet(String name, String oldVal, String newVal) throws Exception {
        assertEquals(oldVal, fieldHack0r.get(name));
        fieldHack0r.set(name, newVal);
        assertEquals(newVal, fieldHack0r.get(name));
    }

    void assertIllegalAccess(String name, String oldVal) throws Exception {
        assertEquals(oldVal, fieldHack0r.get(name));
        try {
            fieldHack0r.set(name, oldVal + "x");
            fail("It should fail because in java you cannot change a static final class field's value");
        }
        catch (RuntimeException e) {
            String pattern = "java.lang.IllegalAccessException: Can not set static final java.lang.String field net.mensemedia.adaptto2017.commons.testing.p2.PublicClass.%s to java.lang.String";
            assertEquals(String.format(pattern, name), e.getMessage());
            assertEquals(IllegalAccessException.class, e.getCause().getClass());
        }
    }

    @Test
    public void test_set_publicField() throws Exception {
        assertGetAndSet("publicField", "vaa1", "vaa2");
    }

    @Test
    public void test_set_protectedField() throws Exception {
        assertGetAndSet("protectedField", "vab1", "vab2");
    }

    @Test
    public void test_set_packageLocalField() throws Exception {
        assertGetAndSet("packageLocalField", "vac1", "vac2");
    }

    @Test
    public void test_set_privateField() throws Exception {
        assertGetAndSet("privateField", "vad1", "vad2");
    }

    @Test
    public void test_set_publicFinalField() throws Exception {
        assertGetAndSet("publicFinalField", "vba1", "vba2");
    }

    @Test
    public void test_set_protectedFinalField() throws Exception {
        assertGetAndSet("protectedFinalField", "vbb1", "vbb2");
    }

    @Test
    public void test_set_packageLocalFinalField() throws Exception {
        assertGetAndSet("packageLocalFinalField", "vbc1", "vbc2");
    }

    @Test
    public void test_set_privateFinalField() throws Exception {
        assertGetAndSet("privateFinalField", "vbd1", "vbd2");
    }


    @Test
    public void test_set_publicStaticField() throws Exception {
        assertGetAndSet("publicStaticField", "vca1", "vca2");
    }

    @Test
    public void test_set_protectedStaticField() throws Exception {
        assertGetAndSet("protectedStaticField", "vcb1", "vcb2");
    }

    @Test
    public void test_set_packageLocalStaticField() throws Exception {
        assertGetAndSet("packageLocalStaticField", "vcc1", "vcc2");
    }

    @Test
    public void test_set_privateStaticField() throws Exception {
        assertGetAndSet("privateStaticField", "vcd1", "vcd2");
    }


    @Test
    public void test_set_publicStaticFinalField() throws Exception {
        assertIllegalAccess("publicStaticFinalField", "vda1");
    }

    @Test
    public void test_set_protectedStaticFinalField() throws Exception {
        assertIllegalAccess("protectedStaticFinalField", "vdb1");
    }

    @Test
    public void test_set_packageLocalStaticFinalField() throws Exception {
        assertIllegalAccess("packageLocalStaticFinalField", "vdc1");
    }

    @Test
    public void test_set_privateStaticFinalField() throws Exception {
        assertIllegalAccess("privateStaticFinalField", "vdd1");
    }

    @Test
    public void test_get_NoSuchField() throws Exception {
        try {
            fieldHack0r.get("i_do_not_exist");
            fail("It should fail because no such field exists");
        }
        catch (RuntimeException e) {
            assertEquals("java.lang.NoSuchFieldException: i_do_not_exist", e.getMessage());
            assertEquals(NoSuchFieldException.class, e.getCause().getClass());
        }
    }

    @Test
    public void test_set_NoSuchField() throws Exception {
        try {
            fieldHack0r.set("i_do_not_exist", "x");
            fail("It should fail because no such field exists");
        }
        catch (RuntimeException e) {
            assertEquals("java.lang.NoSuchFieldException: i_do_not_exist", e.getMessage());
            assertEquals(NoSuchFieldException.class, e.getCause().getClass());
        }
    }
}
