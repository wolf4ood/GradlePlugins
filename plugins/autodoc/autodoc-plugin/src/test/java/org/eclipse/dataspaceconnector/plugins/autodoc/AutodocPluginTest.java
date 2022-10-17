/*
 *  Copyright (c) 2022 Microsoft Corporation
 *
 *  This program and the accompanying materials are made available under the
 *  terms of the Apache License, Version 2.0 which is available at
 *  https://www.apache.org/licenses/LICENSE-2.0
 *
 *  SPDX-License-Identifier: Apache-2.0
 *
 *  Contributors:
 *       Microsoft Corporation - initial API and implementation
 *
 */

package org.eclipse.dataspaceconnector.plugins.autodoc;

import org.gradle.api.Project;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class AutodocPluginTest {
    @Test
    public void pluginRegistersAutodocTask() {
        // Create a test project and apply the plugin
        Project project = ProjectBuilder.builder().build();
        project.getPlugins().apply(AutodocPlugin.class);

        // Verify the result
        var tasks = project.getTasks();
        assertThat(tasks.findByName("autodoc")).isNotNull();
        assertThat(tasks.findByName("mergeManifest")).isNotNull();
    }
}
