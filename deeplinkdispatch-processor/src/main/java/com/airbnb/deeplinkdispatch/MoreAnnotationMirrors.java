package com.airbnb.deeplinkdispatch;

import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;

import java.util.List;

import javax.annotation.Nullable;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.AnnotationValueVisitor;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.SimpleAnnotationValueVisitor7;

import static com.google.auto.common.AnnotationMirrors.getAnnotationValue;

/**
 * Borrowed from Dagger, Copyright Google
 * https://github.com/google/dagger/blob/master/compiler/src/main/java/dagger/internal/codegen
 * /MoreAnnotationMirrors.java
 */
final class MoreAnnotationMirrors {
  private MoreAnnotationMirrors() {
  }

  private static final AnnotationValueVisitor<TypeMirror, Void> AS_TYPE =
      new SimpleAnnotationValueVisitor7<TypeMirror, Void>() {
        @Override
        public TypeMirror visitType(TypeMirror t, Void p) {
          return t;
        }

        @Override
        protected TypeMirror defaultAction(Object o, Void p) {
          throw new TypeNotPresentException(o.toString(), null);
        }
      };

  private static final AnnotationValueVisitor<ImmutableList<AnnotationValue>, String>
      AS_ANNOTATION_VALUES =
      new SimpleAnnotationValueVisitor7<ImmutableList<AnnotationValue>, String>() {
        @Override
        public ImmutableList<AnnotationValue> visitArray(
            List<? extends AnnotationValue> vals, String elementName) {
          return ImmutableList.copyOf(vals);
        }

        @Override
        protected ImmutableList<AnnotationValue> defaultAction(Object o, String elementName) {
          throw new IllegalArgumentException(elementName + " is not an array: " + o);
        }
      };

  /**
   * Returns the list of values represented by an array annotation value.
   *
   * @throws IllegalArgumentException unless {@code annotationValue} represents an array
   */
  static ImmutableList<AnnotationValue> asAnnotationValues(
      AnnotationValue annotationValue) {
    return annotationValue.accept(AS_ANNOTATION_VALUES, null);
  }

  /**
   * Returns the type represented by an annotation value.
   *
   * @throws IllegalArgumentException unless {@code annotationValue} represents a single type
   */
  private static TypeMirror asType(AnnotationValue annotationValue) {
    return AS_TYPE.visit(annotationValue);
  }

  /**
   * Returns the value named {@code name} from {@code annotationMirror}.
   *
   * @throws IllegalArgumentException unless that member represents a single type
   */
  static Iterable<TypeMirror> getTypeValue(AnnotationMirror annotationMirror, String name) {
    return FluentIterable.from(asAnnotationValues(getAnnotationValue(annotationMirror, name)))
        .transform(new Function<AnnotationValue, TypeMirror>() {
          @Nullable @Override public TypeMirror apply(@Nullable AnnotationValue annotationValue) {
            return asType(annotationValue);
          }
        }).toList();
  }
}
