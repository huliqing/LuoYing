package simple;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import name.huliqing.luoying.data.SkillData;
import name.huliqing.luoying.manager.RandomManager;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author huliqing
 */
public class RandomIndexTest {
    
    public RandomIndexTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void RandomIndexTest() {
        SkillData ss = new SkillData();
        for (int i = 0; i < 300; i++) {
            byte idx = ss.getNextRandomIndex();
            assert idx >=0 && idx <= 126;
        }
    }
    @Test
    public void RandomIndexTest2() {
        float value = RandomManager.getValue(126);
        byte idx = 126;
        assert Float.compare(value, RandomManager.getValue(idx)) == 0;
    }
}
