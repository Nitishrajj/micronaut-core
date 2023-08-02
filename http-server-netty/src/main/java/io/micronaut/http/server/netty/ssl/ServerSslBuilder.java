/*
 * Copyright 2017-2020 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.micronaut.http.server.netty.ssl;

import io.micronaut.core.annotation.Experimental;
import io.micronaut.http.ssl.ServerSslConfiguration;
import io.netty.handler.ssl.SslContext;
import io.netty.incubator.codec.quic.QuicSslContext;

import java.util.Optional;

/**
 * Interface that allows for different builder instances to be able to create
 * the SSLContext supplied later to the micronaut http server.
 */
public interface ServerSslBuilder {

    /**
     * @return The SSL configuration
     */
    ServerSslConfiguration getSslConfiguration();

    /**
     * @return Builds the SSL configuration wrapped inside an optional
     */
    Optional<SslContext> build();

    @Experimental
    default Optional<QuicSslContext> buildQuic() {
        throw new UnsupportedOperationException("QUIC not supported");
    }
}
