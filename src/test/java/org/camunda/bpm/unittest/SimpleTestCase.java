/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.camunda.bpm.unittest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.camunda.bpm.engine.test.assertions.ProcessEngineAssertions.assertThat;
import static org.camunda.bpm.engine.test.assertions.ProcessEngineTests.processInstanceQuery;
import static org.camunda.bpm.engine.test.assertions.ProcessEngineTests.runtimeService;
import static org.camunda.bpm.engine.test.assertions.ProcessEngineTests.taskQuery;
import static org.camunda.bpm.engine.test.assertions.ProcessEngineTests.taskService;
import static org.camunda.bpm.engine.test.assertions.ProcessEngineTests.withVariables;

import java.util.Arrays;
import java.util.List;

import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.engine.test.ProcessEngineRule;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author Daniel Meyer
 * @author Martin Schimak
 */
public class SimpleTestCase {

	@Rule
	public ProcessEngineRule rule = new ProcessEngineRule();

	@Test
	@Deployment(resources = { "testProcess.bpmn" })
	public void shouldExecuteProcess() {

		Long searchId = 42l;
		List<Long> someIdList = Arrays.asList(new Long[] { 10l, 28l, searchId });
		
		// Given we create a new process instance
		ProcessInstance processInstance = runtimeService().startProcessInstanceByKey("testProcess",
				withVariables("someIdList", someIdList));
		// Then it should be active
		assertThat(processInstance).isActive();
		// And it should be the only instance
		assertThat(processInstanceQuery().count()).isEqualTo(1);

		List<Task> list = taskQuery().list();
		assertThat(list).hasSize(someIdList.size());
		
		List<Task> searchResult = taskService().createTaskQuery()
				.processVariableValueEquals("someId", searchId)
				.list();
		System.out.println(searchResult);
		assertThat(searchResult).hasSize(1);
		
		// When we complete that task
		// complete(task(processInstance));
		// Then the process instance should be ended
		// assertThat(processInstance).isEnded();
	}

}
