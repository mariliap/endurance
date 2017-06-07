/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * http://glassfish.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */
package research.mpl.backend.gesps.service;

import research.mpl.backend.gesps.Gesp;
import research.mpl.backend.gesps.SituacaoGespEnum;
import research.mpl.backend.gesps.Usuario;

import javax.ejb.Stateless;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

@Stateless
public class DefaultTrelloGespSyncService implements TrelloGespSyncService {

    private static final Logger logger = Logger
            .getLogger(DefaultTrelloGespSyncService.class.getName());

    String anterior = "ninguem";
    private Usuario usuarioLogado;

    @Override
    public Boolean loginGesp(Usuario usuarioGesp){

        System.out.println("LOGOU "+ anterior + " -> " + usuarioGesp.getLoginGesp());
        anterior = usuarioGesp.getLoginGesp();

        usuarioLogado = usuarioGesp;
        System.out.println("login=" + usuarioGesp.getLoginGesp());
        return true;
    }

    @Override
    public Boolean loginTrello(Usuario usuarioTrello){

        System.out.println("LOGOU "+ anterior + " -> " + usuarioTrello.getLoginGesp());
        anterior = usuarioTrello.getLoginGesp();

        usuarioLogado = usuarioTrello;
        System.out.println("login=" + usuarioTrello.getLoginGesp());
        return true;
    }

    @Override
    public List<Gesp> sincronizarTodosOsChamadosSccPorSituacao(
            SituacaoGespEnum situacao) {

        List<Gesp> listaDeGesps = new ArrayList<>();
        if(usuarioLogado != null) {
            System.out.println("sincronizarTodosOsChamadosSccPorSituacao logado=" + usuarioLogado.getLoginGesp());

            Gesp gesp = new Gesp();
            gesp.setId(12345L);

            gesp.setAtendenteGesp("MARILIA");
            gesp.setAtendentesTrello("MARILIA UAHA");
            gesp.setSituacaoGesp(SituacaoGespEnum.SITUACAO_AGUARDANDO_ATENDIMENTO);
            gesp.setDataSolicitacao(new Date());
            gesp.setSituacaoTrello("assas");
            gesp.setSolicitante("asaadd");
            gesp.setDescricao("assadasdgfgfhfhfghgfhfghhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh");
            gesp.setTipoOcorrencia("dsfsdsd");
            listaDeGesps.add(gesp);

//            List<Gesp> gespsOficiais = GespExplorer.start(usuarioLogado.getLoginGesp(), usuarioLogado.getPasswordGesp());
//            listaDeGesps.addAll(gespsOficiais);
        }
        return listaDeGesps;
    }
}
