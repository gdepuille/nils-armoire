package org.picotteland.sales.contexts;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CharacterEncodingFilter;

/**
 * Created by gdepuille on 05/10/14.
 */
@Configuration
public class WebFiltersContext {

    @Bean
    public CharacterEncodingFilter characterEncodingFilter() {
        final CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        characterEncodingFilter.setEncoding("UTF-8");
        characterEncodingFilter.setForceEncoding(true);
        return characterEncodingFilter;
    }
}
