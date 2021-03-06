/*
 * File: SocketBasedRemoteExecutorTests.java
 *
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * The contents of this file are subject to the terms and conditions of 
 * the Common Development and Distribution License 1.0 (the "License").
 *
 * You may not use this file except in compliance with the License.
 *
 * You can obtain a copy of the License by consulting the LICENSE.txt file
 * distributed with this file, or by consulting https://oss.oracle.com/licenses/CDDL
 *
 * See the License for the specific language governing permissions
 * and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file LICENSE.txt.
 *
 * MODIFICATIONS:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 */

package com.oracle.tools.runtime.concurrent.socket;

import com.oracle.tools.deferred.Eventually;

import com.oracle.tools.deferred.listener.DeferredCompletionListener;

import com.oracle.tools.runtime.concurrent.RemoteCallable;
import com.oracle.tools.runtime.concurrent.RemoteRunnable;

import org.junit.Assert;
import org.junit.Test;

import static com.oracle.tools.deferred.DeferredHelper.valueOf;

import static org.hamcrest.CoreMatchers.is;

import java.io.IOException;

import java.net.InetAddress;

import java.util.concurrent.Callable;

/**
 * Functional Tests for {@link com.oracle.tools.runtime.concurrent.socket.SocketBasedRemoteExecutor}s.
 */
public class SocketBasedRemoteExecutorTests
{
    /**
     * Ensure a {@link com.oracle.tools.runtime.concurrent.socket.RemoteExecutorServer} can submit and receive a
     * {@link Callable}.
     */
    @Test
    public void shouldSubmitStaticPingPongRequest() throws InterruptedException
    {
        RemoteExecutorServer server = null;
        RemoteExecutorClient client = null;

        try
        {
            server = new RemoteExecutorServer();

            InetAddress address = server.open();

            client = new RemoteExecutorClient(address, server.getPort());

            client.open();

            DeferredCompletionListener<String> serverResponse = new DeferredCompletionListener<String>(String.class);

            client.submit(new PingPong(), serverResponse);

            Eventually.assertThat(valueOf(serverResponse), is("PONG"));

            DeferredCompletionListener<String> clientResponse = new DeferredCompletionListener<String>(String.class);

            server.submit(new PingPong(), clientResponse);

            Eventually.assertThat(valueOf(clientResponse), is("PONG"));
        }
        catch (IOException e)
        {
            Assert.fail("Failed to process the request due to:\n" + e);
        }
        finally
        {
            if (client != null)
            {
                client.close();
            }

            if (server != null)
            {
                server.close();
            }
        }
    }


    /**
     * Ensure a {@link com.oracle.tools.runtime.concurrent.socket.RemoteExecutorServer} won't accept
     * inner class submissions.
     */
    @Test
    public void shouldNotPermitInnerClassSubmissions() throws InterruptedException
    {
        RemoteExecutorServer server = null;
        RemoteExecutorClient client = null;

        try
        {
            server = new RemoteExecutorServer();

            InetAddress address = server.open();

            client = new RemoteExecutorClient(address, server.getPort());

            client.open();

            DeferredCompletionListener<String> serverResponse = new DeferredCompletionListener<String>(String.class);

            client.submit(new RemoteCallable<String>()
            {
                @Override
                public String call() throws Exception
                {
                    return "PONG";
                }
            }, serverResponse);

            Assert.fail("Anonymous Inner-Classes Can't be used");
        }
        catch (IOException e)
        {
            Assert.fail("Failed to process the request due to:\n" + e);
        }
        catch (IllegalArgumentException e)
        {
            // success!
        }
        finally
        {
            if (client != null)
            {
                client.close();
            }

            if (server != null)
            {
                server.close();
            }
        }
    }


    /**
     * Ensure a {@link RemoteExecutorServer} won't accept submissions without connected clients.
     */
    @Test
    public void shouldNotSubmitWithoutClients() throws InterruptedException
    {
        RemoteExecutorServer server = null;
        RemoteExecutorClient client = null;

        try
        {
            server = new RemoteExecutorServer();

            DeferredCompletionListener<String> clientResponse = new DeferredCompletionListener<String>(String.class);

            try
            {
                server.submit(new PingPong(), clientResponse);

                Assert.fail("Should not be able to submit a RemoteCallable without a connected client");
            }
            catch (IllegalStateException e)
            {
                // success!
            }

            try
            {
                server.submit(new Ping());

                Assert.fail("Should not be able to submit a RemoteRunnable without a connected client");
            }
            catch (IllegalStateException e)
            {
                // success!
            }
        }
        finally
        {
            if (client != null)
            {
                client.close();
            }

            if (server != null)
            {
                server.close();
            }
        }
    }


    /**
     * A simple ping {@link RemoteRunnable}.
     */
    public static class Ping implements RemoteRunnable
    {
        @Override
        public void run()
        {
            System.out.println("PING");
        }
    }


    /**
     * A simple ping-pong {@link RemoteCallable}.
     */
    public static class PingPong implements RemoteCallable<String>
    {
        @Override
        public String call() throws Exception
        {
            return "PONG";
        }
    }
}
