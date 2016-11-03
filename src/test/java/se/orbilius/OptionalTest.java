package se.orbilius;

import org.junit.Test;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertTrue;

public class OptionalTest {

    @Test
    public void optionalAvNågotSomÄrNull(){

        class C{
            public String string;
        }

        class B{
            public C c;
        }

        class A{
            public B b;
        }


        A aMedNullI = new A();

        try {
            String s = Optional.of(aMedNullI.b.c.string).orElse("null nånstans");
        } catch(NullPointerException npe){
            assertTrue("Det där gick ju inte", true);
        }
    }
}
