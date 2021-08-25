package com.emerson.bpm.engine;

import java.util.Collection;
import java.util.Map;

import org.kie.api.KieBase;
import org.kie.api.command.Command;
import org.kie.api.event.process.ProcessEventListener;
import org.kie.api.event.rule.AgendaEventListener;
import org.kie.api.event.rule.RuleRuntimeEventListener;
import org.kie.api.logger.KieRuntimeLogger;
import org.kie.api.runtime.Calendars;
import org.kie.api.runtime.Channel;
import org.kie.api.runtime.Environment;
import org.kie.api.runtime.Globals;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.KieSessionConfiguration;
import org.kie.api.runtime.ObjectFilter;
import org.kie.api.runtime.process.ProcessInstance;
import org.kie.api.runtime.process.WorkItemManager;
import org.kie.api.runtime.rule.Agenda;
import org.kie.api.runtime.rule.AgendaFilter;
import org.kie.api.runtime.rule.EntryPoint;
import org.kie.api.runtime.rule.FactHandle;
import org.kie.api.runtime.rule.FactHandle.State;
import org.kie.api.runtime.rule.LiveQuery;
import org.kie.api.runtime.rule.QueryResults;
import org.kie.api.runtime.rule.ViewChangedEventListener;
import org.kie.api.time.SessionClock;

import com.emerson.bpm.api.Session;
import com.emerson.bpm.api.WorkingMemory;

public class DefaultSession implements KieSession, Session {

	@Override
	public int fireAllRules() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int fireAllRules(int max) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int fireAllRules(AgendaFilter agendaFilter) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int fireAllRules(AgendaFilter agendaFilter, int max) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void fireUntilHalt() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void fireUntilHalt(AgendaFilter agendaFilter) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <T> T execute(Command<T> command) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T extends SessionClock> T getSessionClock() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setGlobal(String identifier, Object value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object getGlobal(String identifier) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Globals getGlobals() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Calendars getCalendars() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Environment getEnvironment() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public KieBase getKieBase() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void registerChannel(String name, Channel channel) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void unregisterChannel(String name) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Map<String, Channel> getChannels() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public KieSessionConfiguration getSessionConfiguration() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void halt() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Agenda getAgenda() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EntryPoint getEntryPoint(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<? extends EntryPoint> getEntryPoints() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public QueryResults getQueryResults(String query, Object... arguments) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LiveQuery openLiveQuery(String query, Object[] arguments, ViewChangedEventListener listener) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getEntryPointId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FactHandle insert(Object object) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void retract(FactHandle handle) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(FactHandle handle) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(FactHandle handle, State fhState) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(FactHandle handle, Object object) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(FactHandle handle, Object object, String... modifiedProperties) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public FactHandle getFactHandle(Object object) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getObject(FactHandle factHandle) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<? extends Object> getObjects() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<? extends Object> getObjects(ObjectFilter filter) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T extends FactHandle> Collection<T> getFactHandles() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T extends FactHandle> Collection<T> getFactHandles(ObjectFilter filter) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getFactCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ProcessInstance startProcess(String processId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ProcessInstance startProcess(String processId, Map<String, Object> parameters) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ProcessInstance startProcess(String processId, AgendaFilter agendaFilter) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ProcessInstance startProcess(String processId, Map<String, Object> parameters, AgendaFilter agendaFilter) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ProcessInstance startProcessFromNodeIds(String processId, Map<String, Object> params, String... nodeId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ProcessInstance createProcessInstance(String processId, Map<String, Object> parameters) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ProcessInstance startProcessInstance(long processInstanceId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void signalEvent(String type, Object event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void signalEvent(String type, Object event, long processInstanceId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Collection<ProcessInstance> getProcessInstances() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ProcessInstance getProcessInstance(long processInstanceId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ProcessInstance getProcessInstance(long processInstanceId, boolean readonly) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void abortProcessInstance(long processInstanceId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public WorkItemManager getWorkItemManager() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public KieRuntimeLogger getLogger() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addEventListener(RuleRuntimeEventListener listener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeEventListener(RuleRuntimeEventListener listener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Collection<RuleRuntimeEventListener> getRuleRuntimeEventListeners() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addEventListener(AgendaEventListener listener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeEventListener(AgendaEventListener listener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Collection<AgendaEventListener> getAgendaEventListeners() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addEventListener(ProcessEventListener listener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeEventListener(ProcessEventListener listener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Collection<ProcessEventListener> getProcessEventListeners() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getId() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getIdentifier() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void submit(AtomicAction action) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <T> T getKieRuntime(Class<T> cls) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public WorkingMemory getWorkingMemory() {
		// TODO Auto-generated method stub
		return null;
	}

}
