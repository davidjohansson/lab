package se.orbilius;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class DivTest {


	public enum EMIDefinitions {

		INSTANCE;

		private final String[] contentTypesForPublication = {"a", "b", "c"};
		private final String[] excludeFromUnpublication = {"c", "d"};

		public String[] getContentTypesForPublication() {
			return contentTypesForPublication;
		}

		public String[] getContentTypesForUnPublication() {
			Collection<String> intersection = CollectionUtils.<String>subtract(Arrays.asList(contentTypesForPublication), Arrays.asList(excludeFromUnpublication));
			return intersection.toArray(new String[intersection.size()]);
		}
		EMIDefinitions() {}
	}


	@Test
	public void shouldTestSets() throws Exception {
		// given
		// when

		String[] c = EMIDefinitions.INSTANCE.getContentTypesForUnPublication();

		Arrays.asList(c).stream().forEach(s -> System.out.println(s));

		// then
		assertThat(c.length, is(2));
		assertThat(c[0], equalTo("a"));
		assertThat(c[1], equalTo("b"));
		
	}
}
