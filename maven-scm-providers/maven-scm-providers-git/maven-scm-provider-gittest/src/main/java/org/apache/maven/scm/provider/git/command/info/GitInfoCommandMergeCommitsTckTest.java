/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.maven.scm.provider.git.command.info;

import java.io.File;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import org.apache.maven.scm.CommandParameter;
import org.apache.maven.scm.CommandParameters;
import org.apache.maven.scm.PlexusJUnit4TestCase;
import org.apache.maven.scm.command.info.InfoItem;
import org.apache.maven.scm.command.info.InfoScmResult;
import org.apache.maven.scm.provider.ScmProvider;
import org.apache.maven.scm.provider.git.GitScmTestUtils;
import org.apache.maven.scm.tck.command.info.InfoCommandTckTest;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author <a href="mailto:lpatton0@gmail.com">Luke Patton</a>
 */
public abstract class GitInfoCommandMergeCommitsTckTest extends InfoCommandTckTest {
    /**
     * {@inheritDoc}
     */
    public void initRepo() throws Exception {
        GitScmTestUtils.initRepo("src/test/resources/repository/", getRepositoryRoot(), getWorkingDirectory());
    }
    /*public void initRepo() throws Exception {
        GitScmTestUtils.initRepo("src/test/resources/repository-with-merge-commit/", getRepositoryRoot(), getWorkingDirectory());
    }*/

    protected File getRepositoryRoot() {
        return PlexusJUnit4TestCase.getTestFile("target/scm-test/repository");
    }
    /*protected File getRepositoryRoot() {
        return PlexusJUnit4TestCase.getTestFile("target/scm-test/repository-with-merge-commit");
    }*/

    /*protected File getRepository() {
        return PlexusJUnit4TestCase.getTestFile("/src/test/repository-with-merge-commit");
    }*/

    @Test
    public void testGitInfoCommandWithSkipMergeCommits() throws Exception {
        ScmProvider scmProvider = getScmManager().getProviderByUrl(getScmUrl());
        CommandParameters parameters = new CommandParameters();
        parameters.setString(CommandParameter.SCM_SKIP_MERGE_COMMITS, "true");
        InfoScmResult result =
                scmProvider.info(getScmRepository().getProviderRepository(), getScmFileSet(), parameters);
        assertResultIsSuccess(result);
        assertEquals(1, result.getInfoItems().size());
        InfoItem item = result.getInfoItems().get(0);
        assertEquals("Luke Patton <lpatton0@gmail.com>", item.getLastChangedAuthor());
        assertEquals("ea25d181203827d5ac1b9a89d7e1d2c66b27b9d9", item.getRevision());
        assertEquals(
                OffsetDateTime.of(2025, 12, 28, 16, 13, 48, 0, ZoneOffset.ofHours(-5)), item.getLastChangedDateTime());
    }

    @Test
    public void testGitInfoCommandWithoutSkippingMergeCommits() throws Exception {
        ScmProvider scmProvider = getScmManager().getProviderByUrl(getScmUrl());
        CommandParameters parameters = new CommandParameters();
        parameters.setString(CommandParameter.SCM_SKIP_MERGE_COMMITS, "false");
        InfoScmResult result =
                scmProvider.info(getScmRepository().getProviderRepository(), getScmFileSet(), parameters);
        assertResultIsSuccess(result);
        assertEquals(1, result.getInfoItems().size());
        InfoItem item = result.getInfoItems().get(0);
        assertEquals("Luke Patton <lpatton0@gmail.com>", item.getLastChangedAuthor());
        assertEquals("ea25d181203827d5ac1b9a89d7e1d2c66b27b9d9", item.getRevision());
        assertEquals(
                OffsetDateTime.of(2025, 12, 28, 16, 13, 48, 0, ZoneOffset.ofHours(-5)), item.getLastChangedDateTime());
    }
}
