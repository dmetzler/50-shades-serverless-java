/*
 * (C) Copyright 2019 Nuxeo (http://nuxeo.com/).
 * This is unpublished proprietary source code of Nuxeo SA. All rights reserved.
 * Notice of copyright on this source code does not indicate publication.
 *
 * Contributors:
 *     dmetzler
 */
package org.dmetzler.serverless.movie;

import static io.restassured.path.json.JsonPath.with;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.Consumer;

import javax.ws.rs.core.Response.Status;

import org.assertj.core.api.AbstractCharSequenceAssert;

import com.amazonaws.serverless.proxy.model.AwsProxyResponse;

public class ResponseAssert {

    private AwsProxyResponse response;

    public ResponseAssert(AwsProxyResponse response) {
        this.response = response;
        assertThat(response).isNotNull();
        assertThat(response.isBase64Encoded()).isFalse();

    }

    public ResponseAssert body(String prop, Consumer<AbstractCharSequenceAssert<?, String>> a) {
        a.accept(assertThat(with(response.getBody()).getString(prop)));
        return this;

    }

    public ResponseAssert headers(String header, Consumer<AbstractCharSequenceAssert<?, String>> a) {
        assertThat(response.getMultiValueHeaders()).isNotNull();
        assertThat(response.getMultiValueHeaders()).containsKey(header);
        a.accept(assertThat(response.getMultiValueHeaders().getFirst(header)));
        return this;
    }

    public ResponseAssert status(Status status) {
        assertThat(response.getStatusCode()).isEqualTo(status.getStatusCode());
        return this;
    }

}
