/**
 * vertigo - simple java starter
 *
 * Copyright (C) 2013-2019, vertigo-io, KleeGroup, direction.technique@kleegroup.com (http://www.kleegroup.com)
 * KleeGroup, Centre d'affaire la Boursidiere - BP 159 - 92357 Le Plessis Robinson Cedex - France
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.vertigo.connectors.mail;

import java.util.Optional;

import javax.inject.Inject;
import javax.mail.Session;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import io.vertigo.core.lang.Assertion;
import io.vertigo.core.lang.WrappedException;
import io.vertigo.core.param.ParamValue;

/**
 * Plugin d'accès au serveur mail, utilisant une resource Jndi.
 *
 * @author npiedeloup
 */
public final class JndiMailSessionConnector implements MailSessionConnector {

	private final String connectorName;
	private final String jndiMailSession;

	/**
	 * Crée le plugin d'accès au serveur mail.
	 * @param jndiMailSession Url jndi de la resource du serveur mail
	 */
	@Inject
	public JndiMailSessionConnector(
			@ParamValue("name") final Optional<String> connectorNameOpt,
			@ParamValue("mail.session") final String jndiMailSession) {
		Assertion.check()
				.isNotNull(connectorNameOpt)
				.isNotBlank(jndiMailSession)
				.isTrue(jndiMailSession.startsWith("java:comp/env"), "{0} (mail.session) is the jndi url of mail server resource. Must start by 'java:comp/env'", jndiMailSession);
		//-----
		connectorName = connectorNameOpt.orElse(DEFAULT_CONNECTOR_NAME);
		//-----
		this.jndiMailSession = jndiMailSession;
	}

	@Override
	public String getName() {
		return connectorName;
	}

	@Override
	public Session getClient() {
		Session mailSession;
		try {
			mailSession = (Session) new InitialContext().lookup(jndiMailSession);
		} catch (final NamingException e) {
			throw WrappedException.wrap(e, "Can't obtain MailResource : {0}", jndiMailSession);
		}
		return mailSession;
	}
}
