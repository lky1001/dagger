/*
 * Copyright (C) 2018 The Dagger Authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dagger.internal.codegen;

import static com.squareup.javapoet.TypeSpec.classBuilder;
import static javax.lang.model.element.Modifier.FINAL;
import static javax.lang.model.element.Modifier.PUBLIC;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeSpec;
import java.util.Optional;
import javax.annotation.processing.Filer;
import javax.inject.Inject;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;

/**
 * Generates a class that exposes a non-{@code public} {@link ContributionBinding#mapKey()} @MapKey}
 * annotation.
 */
final class InaccessibleMapKeyProxyGenerator extends SourceFileGenerator<ContributionBinding> {
  private final DaggerTypes types;

  @Inject
  InaccessibleMapKeyProxyGenerator(
      Filer filer, DaggerTypes types, DaggerElements elements, SourceVersion sourceVersion) {
    super(filer, elements, sourceVersion);
    this.types = types;
  }

  @Override
  ClassName nameGeneratedType(ContributionBinding binding) {
    return MapKeys.mapKeyProxyClassName(binding);
  }

  @Override
  Optional<? extends Element> getElementForErrorReporting(ContributionBinding binding) {
    return binding.bindingElement();
  }

  @Override
  Optional<TypeSpec.Builder> write(ClassName generatedName, ContributionBinding binding) {
    return MapKeys.mapKeyFactoryMethod(binding, types)
        .map(method -> classBuilder(generatedName).addModifiers(PUBLIC, FINAL).addMethod(method));
  }
}
