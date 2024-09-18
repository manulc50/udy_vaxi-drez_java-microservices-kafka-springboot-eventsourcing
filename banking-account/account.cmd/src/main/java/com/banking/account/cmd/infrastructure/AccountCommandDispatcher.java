package com.banking.account.cmd.infrastructure;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.banking.cqrs.core.commands.BaseCommand;
import com.banking.cqrs.core.commands.CommandHandlerMethod;
import com.banking.cqrs.core.infrastructure.CommandDispatcher;

@Service
public class AccountCommandDispatcher implements CommandDispatcher<BaseCommand> {
	
	private final Map<Class<? extends BaseCommand>, List<CommandHandlerMethod<BaseCommand>>> routes = new HashMap<>();

	@Override
	public void registerHandler(Class<BaseCommand> type, CommandHandlerMethod<BaseCommand> handler) {
		var handlers = routes.computeIfAbsent(type, c -> new LinkedList<>()); // En este caso, "var handlers" se traduce a "List<CommandHandlerMethod> handlers"
		handlers.add(handler);
	}

	@Override
	public void send(BaseCommand command) {
		var handlers = routes.get(command.getClass());
		
		if(handlers == null || handlers.size() == 0)
			throw new RuntimeException("El command handler no fue registrado");
		
		if(handlers.size() > 1)
			throw new RuntimeException("No puede enviar un command que tiene más de un handler");
		
		handlers.get(0).handle(command);
		
	}

}
