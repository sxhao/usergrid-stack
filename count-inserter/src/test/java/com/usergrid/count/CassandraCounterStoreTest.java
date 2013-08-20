/*******************************************************************************
 * Copyright 2012 Apigee Corporation
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.usergrid.count;

import com.usergrid.count.common.Count;
import me.prettyprint.cassandra.service.CassandraHostConfigurator;
import me.prettyprint.hector.api.Cluster;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.testutils.EmbeddedServerHelper;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static junit.framework.Assert.assertEquals;

/**
 * @author zznate
 */
public class CassandraCounterStoreTest {

    private static EmbeddedServerHelper esh;
    private static Cluster cluster;

    @BeforeClass
    public static void setup() throws Exception {
        esh = new EmbeddedServerHelper();
        esh.setup();
        cluster = HFactory.getOrCreateCluster("CounterTestCluster", new CassandraHostConfigurator("localhost:9170"));
    }

    @Test
    public void testSerializer() {
        CassandraCounterStore cassandraCounterStore =
                new CassandraCounterStore(HFactory.createKeyspace("Keyspace1", cluster));
        Count count = new Count("Counter1","k1","c1",1);
        cassandraCounterStore.save(count);
    }

    @Test
    public void testApplyCount() {
        Count count = new Count("Counters","k1","c1",1);
        Count c2 = new Count("Counters","k1","c1",1);
        Count c3 = new Count("Counters","k1","c1",1);
        count.apply(c2);
        assertEquals(2, count.getValue());
    }
    @Test
    public void testSave() {
        CassandraCounterStore cassandraCounterStore =
                new CassandraCounterStore(HFactory.createKeyspace("Keyspace1", cluster));
        Collection<Count> counts = new ArrayList<Count>();
        Count count = new Count("Counter1","k1","c1",1);
        counts.add(count);
        cassandraCounterStore.save(counts);
        Map<String,Count> countHolder = new HashMap<String, Count>();
        for ( Count countlocal : counts ) {
            Count c = countHolder.get(countlocal.getCounterName());
            if (c!= null){

            }
        }
    }
    @AfterClass
    public static void teardown() throws IOException {
        EmbeddedServerHelper.teardown();
    }
}
