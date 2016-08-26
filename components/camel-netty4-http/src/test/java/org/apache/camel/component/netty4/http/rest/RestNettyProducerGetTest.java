/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.camel.component.netty4.http.rest;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.netty4.http.BaseNettyTest;
import org.junit.Test;

public class RestNettyProducerGetTest extends BaseNettyTest {

    @Test
    public void testNettyProducerGet() throws Exception {
        String out = fluentTemplate.withHeader("id", "123").to("direct:start").request(String.class);
        assertNotNull(out);
        // TODO: [123, {id}];Donald Duck
        // assertEquals("123;Donald Duck", out);
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                // configure to use netty on localhost with the given port
                restConfiguration().component("netty4-http").host("localhost").port(getPort());

                from("direct:start")
                        .to("rest:get:users/{id}/basic");

                // use the rest DSL to define the rest services
                rest("/users/")
                        .get("{id}/basic")
                        .route()
                        .to("mock:input")
                        .process(new Processor() {
                            public void process(Exchange exchange) throws Exception {
                                String id = exchange.getIn().getHeader("id", String.class);
                                exchange.getOut().setBody(id + ";Donald Duck");
                            }
                        });
            }
        };
    }

}
