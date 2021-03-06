package com.catinthedark.flash_transmitter.lib.algorithm;

import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
/**
 * Created by Ilya on 16.04.2014.
 */
public class RawDataTranslatorTest {
    private RawDataTranslator dataTranslator;
    private ArrayList<Byte> dataList;
    private ArrayList<Byte> dataList_5;
    private ArrayList<Byte> dataList_6;

    final Long[] timestamps_1 = {1395576696334L, 1395576696632L, 1395576696732L, 1395576697033L, 1395576697132L, 1395576697432L, 1395576697643L, 1395576697842L, 1395576698042L, 1395576698342L, 1395576698442L, 1395576698744L, 1395576698842L, 1395576699143L, 1395576699242L, 1395576699753L, 1395576699852L, 1395576700152L, 1395576700253L, 1395576700552L, 1395576700852L, 1395576701462L, 1395576701762L, 1395576702062L, 1395576702162L, 1395576702662L};
    final Long[] timestamps_2 = {1395576720543L, 1395576720943L, 1395576721143L, 1395576721542L, 1395576721743L, 1395576722043L, 1395576722353L, 1395576722653L, 1395576722853L, 1395576723252L, 1395576723452L, 1395576723863L, 1395576724062L, 1395576724464L, 1395576724662L, 1395576725263L, 1395576725472L, 1395576725873L, 1395576726073L, 1395576726472L, 1395576726972L, 1395576727582L, 1395576728083L, 1395576728482L, 1395576728682L, 1395576729392L};
    final Long[] timestamps_3 = {1395576928803L, 1395576929102L, 1395576929202L, 1395576929502L, 1395576929703L, 1395576930003L, 1395576930213L, 1395576930512L, 1395576930712L, 1395576931012L, 1395576931212L, 1395576931512L, 1395576931712L, 1395576932023L, 1395576932123L, 1395576932723L, 1395576932822L, 1395576933223L, 1395576933322L, 1395576933633L, 1395576934032L, 1395576934632L, 1395576935032L, 1395576935342L, 1395576935543L, 1395576936042L};
    // timestamp_3 potentials [inf, 299, 100, 300, 201, 300, 210, 299, 200, 300, 200, 300, 200, 311, 100, 600, 99, 401, 99, 311, 399, 600, 400, 310, 201, 499, inf]
    final Long[] timestamps_4 = {1395576995392L, 1395576995792L, 1395576995992L, 1395576996393L, 1395576996603L, 1395576996902L, 1395576997202L, 1395576997502L, 1395576997702L, 1395576998102L, 1395576998312L, 1395576998712L, 1395576998913L, 1395576999312L, 1395576999512L, 1395577000122L, 1395577000322L, 1395577000722L, 1395577000922L, 1395577001322L, 1395577001832L, 1395577002432L, 1395577003032L, 1395577003332L, 1395577003542L, 1395577004242L};
    // timestamps_4 potentials [inf, 400, 200, 401, 210, 299, 300, 300, 200, 400, 210, 400, 201, 399, 200, 610, 200, 400, 200, 400, 510, 600, 600, 300, 210, 700, inf]

    final Long[] timestamps_5 = {1395579470563L, 1395579470863L, 1395579471063L, 1395579471462L, 1395579471672L, 1395579472072L, 1395579472273L, 1395579472672L, 1395579472872L, 1395579473173L, 1395579473482L, 1395579473782L, 1395579473982L, 1395579474382L, 1395579474583L, 1395579475283L, 1395579475493L, 1395579475892L, 1395579476092L, 1395579476392L, 1395579476893L, 1395579477602L, 1395579478102L, 1395579478403L, 1395579478703L, 1395579479313L, 1395579479912L, 1395579480512L, 1395579481024L, 1395579481722L, 1395579481923L, 1395579482223L, 1395579482722L, 1395579483132L, 1395579483333L, 1395579483932L, 1395579484233L, 1395579484533L, 1395579484743L, 1395579485142L, 1395579485642L, 1395579486042L, 1395579486242L, 1395579486852L, 1395579487352L, 1395579487752L, 1395579487952L, 1395579488662L, 1395579488863L, 1395579489163L, 1395579489363L, 1395579489762L, 1395579490263L, 1395579490672L, 1395579490872L, 1395579491473L, 1395579491973L, 1395579492382L, 1395579492583L, 1395579493282L, 1395579493782L, 1395579494192L, 1395579494392L, 1395579494792L, 1395579494992L, 1395579495292L, 1395579495592L, 1395579496202L, 1395579496703L, 1395579497103L, 1395579497303L, 1395579498012L};
    final Long[] timestamps_6 = {10363L,10763L,11973L,12573L,13283L,13783L,14493L,15093L,15793L,16403L,17102L,18213L,19513L,20013L,20723L,21323L,22033L,23133L,23844L,24443L,25653L,26253L,26953L,27563L,28263L,28863L};
    final Byte[] data_5 = {0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 1, 0, 1, 0, 1, 0, 0, 1, 1, 0, 0, 1, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 1, 0, 0, 1, 0, 1, 1, 0, 1, 0, 1, 0, 0, 1, 0, 1, 1, 0, 0, 1, 0, 1, 1, 0, 1, 0, 1, 0, 0, 1, 0, 1, 1, 0, 0, 1, 0, 1, 1, 0, 0, 1, 0, 1, 0, 1, 0, 1, 1, 0, 0, 1, 0, 1, 1, 0};
    final Byte[] data_6 = {0, 1,0,0,1,0,1,0,1,0,1,0,1,1,0,0,1,0,1,0,1,1,0,1,0,0,1,0,1,0,1, 0};
    final Byte[] data = {0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,1,0,1,0,1,0,0,1,1,0,0,1,0,1,1,0};

    @Before
    public void setup() {
        dataTranslator = new RawDataTranslator();
        dataList = new ArrayList<Byte>(Arrays.asList(data));
        dataList_5 = new ArrayList<Byte>(Arrays.asList(data_5));
        dataList_6 = new ArrayList<Byte>(Arrays.asList(data_6));
    }

    /*
    @Test
    public void testTranslate1() {
        ArrayList<Long> times = new ArrayList<Long>(Arrays.asList(timestamps_1));
        Byte[] bits = dataTranslator.translate(times);

        Assert.assertEquals(dataList, Arrays.asList(bits));
    }
    */

    @Test
    public void testTranslate2() {
        ArrayList<Long> times = new ArrayList<Long>(Arrays.asList(timestamps_2));
        Byte[] bits = dataTranslator.translate(times);

        Assert.assertEquals(dataList, Arrays.asList(bits));
    }

    /*
    @Test
    public void testTranslate3() {
        ArrayList<Long> times = new ArrayList<Long>(Arrays.asList(timestamps_3));
        Byte[] bits = dataTranslator.translate(times);

        Assert.assertEquals(dataList, Arrays.asList(bits));
    }
    */

    @Test
    public void testTranslate4() {
        ArrayList<Long> times = new ArrayList<Long>(Arrays.asList(timestamps_4));
        Byte[] bits = dataTranslator.translate(times);

        Assert.assertEquals(dataList, Arrays.asList(bits));
    }

    @Test
    public void testTranslate5() {
        ArrayList<Long> times = new ArrayList<Long>(Arrays.asList(timestamps_5));
        Byte[] bits = dataTranslator.translate(times);

        Assert.assertEquals(dataList_5, Arrays.asList(bits));
    }

    @Test
    public void testTranslate6() {
        ArrayList<Long> times = new ArrayList<Long>(Arrays.asList(timestamps_6));
        Byte[] bits = dataTranslator.translate(times);

        Assert.assertEquals(dataList_6, Arrays.asList(bits));
    }
}
