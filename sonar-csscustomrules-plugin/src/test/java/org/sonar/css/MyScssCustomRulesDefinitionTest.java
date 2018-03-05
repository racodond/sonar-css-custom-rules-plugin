/*
 * SonarQube CSS / SCSS / Less Custom Rules Plugin
 * Copyright (C) 2016-2018 David RACODON
 * david.racodon@gmail.com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.sonar.css;

import org.junit.Test;
import org.sonar.api.server.rule.RulesDefinition;
import org.sonar.check.Rule;
import org.sonar.css.checks.less.InterpolatedPropertiesCheck;

import static org.fest.assertions.Assertions.assertThat;

public class MyScssCustomRulesDefinitionTest {

  @Test
  public void test() {
    MyScssCustomRulesDefinition rulesDefinition = new MyScssCustomRulesDefinition();

    RulesDefinition.Context context = new RulesDefinition.Context();
    rulesDefinition.define(context);

    RulesDefinition.Repository repository = context.repository("customscss");

    assertThat(repository.name()).isEqualTo("My SCSS Custom Repository");
    assertThat(repository.language()).isEqualTo("scss");
    assertThat(repository.rules()).hasSize(1);

    RulesDefinition.Rule interpolatedPropertiesRule = repository.rule(InterpolatedPropertiesCheck.class.getAnnotation(Rule.class).key());
    assertThat(interpolatedPropertiesRule).isNotNull();
    assertThat(interpolatedPropertiesRule.name()).isEqualTo(InterpolatedPropertiesCheck.class.getAnnotation(Rule.class).name());
  }

}
