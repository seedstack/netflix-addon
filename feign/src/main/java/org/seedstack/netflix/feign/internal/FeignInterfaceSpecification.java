/**
 * Copyright (c) 2013-2016, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.netflix.feign.internal;

import feign.RequestLine;
import org.kametic.specifications.AbstractSpecification;
import org.seedstack.netflix.feign.FeignClient;
import org.seedstack.shed.reflect.AnnotationPredicates;
import org.seedstack.shed.reflect.ClassPredicates;

/**
 * The specification matches classes that are Interfaces and have methods annotated with @{@link RequestLine}
 *
 * @author adrien.domurado@gmail.com
 */
class FeignInterfaceSpecification extends AbstractSpecification<Class<?>> {
    @Override
    public boolean isSatisfiedBy(Class<?> candidate) {
        return ClassPredicates
                .classIsInterface()
                .and(AnnotationPredicates.elementAnnotatedWith(FeignClient.class, false))
                .and(AnnotationPredicates.atLeastOneMethodAnnotatedWith(RequestLine.class, false))
                .test(candidate);
    }
}
