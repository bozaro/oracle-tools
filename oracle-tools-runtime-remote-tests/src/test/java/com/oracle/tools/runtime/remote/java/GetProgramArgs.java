/*
 * File: GetProgramArgs.java
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

package com.oracle.tools.runtime.remote.java;

import com.oracle.tools.runtime.concurrent.RemoteCallable;

import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;

/**
 * A {@link com.oracle.tools.runtime.concurrent.RemoteCallable} implementation that get all of the arguments
 * used to start the process.
 * <p>
 * Copyright (c) 2014. All Rights Reserved. Oracle Corporation.<br>
 * Oracle is a registered trademark of Oracle Corporation and/or its affiliates.
 *
 * @author Jonathan Knight
 */
public class GetProgramArgs implements RemoteCallable<List<String>>
{
    /**
     * Obtain the arguments used to start this process.
     *
     * @return the arguments used to start this process
     */
    @Override
    public List<String> call() throws Exception
    {
        List<String> args = new ArrayList<String>();
        try
        {
            args.addAll(ManagementFactory.getRuntimeMXBean().getInputArguments());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return args;
    }
}
