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

import io.vertigo.core.node.config.Feature;
import io.vertigo.core.node.config.Features;
import io.vertigo.core.param.Param;

/**
 * Defines commons module.
 * @author pchretien
 */
public final class MailFeatures extends Features<MailFeatures> {

	/**
	 * Constructor.
	 */
	public MailFeatures() {
		super("vertigo-mail-connector");
	}

	@Feature("javax.native")
	public MailFeatures withNativeMailConnector(final Param... params) {
		getModuleConfigBuilder()
				.addConnector(NativeMailSessionConnector.class, params);

		return this;
	}

	@Feature("javax.jndi")
	public MailFeatures withJndiMailConnector(final Param... params) {
		getModuleConfigBuilder()
				.addConnector(JndiMailSessionConnector.class, params);

		return this;
	}

	/** {@inheritDoc} */
	@Override
	protected void buildFeatures() {
		//
	}
}
