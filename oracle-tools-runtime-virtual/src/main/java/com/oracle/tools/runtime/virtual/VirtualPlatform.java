/*
 * File: VirtualMachinePlatform.java
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

package com.oracle.tools.runtime.virtual;

import com.oracle.tools.runtime.remote.Authentication;
import com.oracle.tools.runtime.remote.RemotePlatform;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetAddress;

/**
 * A {@link com.oracle.tools.runtime.Platform} implementation that represents
 * an O/S running in a virtual machine.
 * <p>
 * Copyright (c) 2014. All Rights Reserved. Oracle Corporation.<br>
 * Oracle is a registered trademark of Oracle Corporation and/or its affiliates.
 *
 * @author Jonathan Knight
 */
public abstract class VirtualPlatform extends RemotePlatform implements Closeable
{
    /** The action to perform when this {@link } is closed */
    private CloseAction closeAction;


    /**
     * Construct a new {@link VirtualPlatform}.
     *
     * @param name           the name of this {@link VirtualPlatform}
     * @param address         the {@link InetAddress} that will be used to SSH
     *                        into this {@link VirtualPlatform}
     * @param port            the remote port that will be used to SSH into
     *                        this {@link VirtualPlatform}
     * @param userName        the user name on the remote host
     * @param authentication  the {@link Authentication} for connecting to the host
     * @param closeAction     the action to perform when this {@link VirtualPlatform}
     *                        is closed
     */
    public VirtualPlatform(String name,
                           InetAddress address,
                           int port,
                           String userName,
                           Authentication authentication,
                           CloseAction closeAction)
    {
        super(name, address, port, userName, authentication);
        this.closeAction = closeAction != null ? closeAction : CloseAction.Shutdown;
    }


    /**
     * Close this VirtualPlatform by performing the
     * action specified by the {@link #closeAction} field.
     *
     * @throws IOException
     *
     * @see java.io.Closeable
     */
    @Override
    public abstract void close() throws IOException;


    /**
     * Obtain the {@link CloseAction} to perform when this {@link VirtualPlatform}
     * is closed.
     *
     * @return the {@link CloseAction} to perform when this {@link VirtualPlatform}
     *         is closed
     */
    public CloseAction getCloseAction()
    {
        return closeAction;
    }
}
