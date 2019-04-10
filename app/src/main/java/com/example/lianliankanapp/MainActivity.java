package com.example.lianliankanapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    int  N = 8;     //界面上格子的个数N*N
    int  M = 10;    //物体的种类数
    //定义存放界面图片按钮的数组
    int[] btnIds ={
            R.id.button_11,R.id.button_12,R.id.button_13,R.id.button_14,R.id.button_15,R.id.button_16,R.id.button_17,R.id.button_18
            ,R.id.button_21,R.id.button_22,R.id.button_23,R.id.button_24,R.id.button_25,R.id.button_26,R.id.button_27,R.id.button_28
            ,R.id.button_31,R.id.button_32,R.id.button_33,R.id.button_34,R.id.button_35,R.id.button_36,R.id.button_37,R.id.button_38
            ,R.id.button_41,R.id.button_42,R.id.button_43,R.id.button_44,R.id.button_45,R.id.button_46,R.id.button_47,R.id.button_48
            ,R.id.button_51,R.id.button_52,R.id.button_53,R.id.button_54,R.id.button_55,R.id.button_56,R.id.button_57,R.id.button_58
            ,R.id.button_61,R.id.button_62,R.id.button_63,R.id.button_64,R.id.button_65,R.id.button_66,R.id.button_67,R.id.button_68
            ,R.id.button_71,R.id.button_72,R.id.button_73,R.id.button_74,R.id.button_75,R.id.button_76,R.id.button_77,R.id.button_78
            ,R.id.button_81,R.id.button_82,R.id.button_83,R.id.button_84,R.id.button_85,R.id.button_86,R.id.button_87,R.id.button_88

    };
    //定义存放每种物体的图片id数组
    int[] imageIds ={
            R.drawable.s1,R.drawable.s2,R.drawable.s3,R.drawable.s4,R.drawable.s5,R.drawable.s6,R.drawable.s7
            ,R.drawable.s8,R.drawable.s9,R.drawable.s10
    };
    //定义存放界面上按钮对象的数组
    ImageButton[]   btns   = new ImageButton[N*N];
    //定义表示界面上N*N个格子的状态数组
    int[][] pos = new int[N][N];    //0---表示没有物体
                                        //1-M物体的种类
    //定义记录用户点击次序的标志
    int clickFlag=0;
    //定义记录两次点击的按钮的位置序号
    int btnIndex1,btnIndex2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //加载按钮
        for(int i=0;i<btnIds.length;i++){
            btns[i]  = (ImageButton) findViewById( btnIds[i] );
            //设置按钮的事件监听
            btns[i].setOnClickListener(this);
        }
        //设置按钮图片
        initButtons();
    }//end onCreate

    //initButtons 按钮初始化
    private void initButtons(){
//        Random rd  = new Random();
//        for(int i=0;i<btnIds.length;i++){
//            int  x  = rd.nextInt(M);
//            btns[i] .setImageDrawable(getResources().getDrawable( imageIds[x]));
//        }
        //定义存放每种物体个数的数组nPerType
        int[] nPerType = new int[M];
        //1.计算每种物体的个数 --偶数  最多相差2个
        //1.1计算绝对平均数
        int avg = N*N/M;
        //1.2判断是否是奇数
        if (avg%2>0)
            avg--;
        //1.3每种物体放置avg个
        for (int i=0;i<M;i++)
            nPerType[i]=avg;
        //1.4分配剩下的零头
        int nLeft = N*N-M*avg;
        //1.5将剩下的零头2个为一组，分配给前面种类的物体
        int nGroup = nLeft/2;
        for (int i=0;i<nGroup;i++){
            nPerType[i]+=2; //前面的种类数+2

        }
        //2.将每种物体随机放置在界面上 ---设置pos[][]的值
        Random rd = new Random();
        for (int i=0;i<M;i++){
            int n = nPerType[i]; //取第i种物体的个数
            //对于n个物体，随机放置一个位置
            for (int j=0;j<n;j++){
                int row = rd.nextInt(N);    //随机获取行号
                int col = rd.nextInt(N);    //随机获取列号
                if(pos[row][col]==0){       //该位置是空位置
                    pos[row][col]=i+1;          //设置该位置放置的物体种类序号
                }else {
                    j--;
                }
            }//end for j
        }//end for i

        //3.根据pos[][]的值，显示屏幕上按钮显示的图片
        for (int i=0;i<N;i++){
            for (int j=0;j<N;j++){
                int status = pos[i][j];     //取格子上(i,j)位置上的值
                if (status>0)
                    //设置（i,j）对应按钮上要显示的图片
                    btns[i*N+j].setImageDrawable(getResources().getDrawable(imageIds[status-1]));
                else    //为0表示该位置没有物体，则不显示按钮
                    btns[i*N+j].setVisibility(View.INVISIBLE);
            }
        }
    }//end initButton

    @Override
    public void onClick(View arg0) {
        //1.获得被点击的按钮的位置序号
        //将被点击按钮的id与所有按钮id进行比较
        for (int i=0;i<btnIds.length;i++){
            if(arg0.getId()==btnIds[i]){
                clickFlag++;
                if (clickFlag==1){      //第一次点击
                    btnIndex1=i;
                }else {                   //第二次点击
                    btnIndex2=i;
                    checkDisappear();       //进行消除的判断
                    clickFlag=0;
                }
                break;
            }
        }//end for
    }//end onClick
    //定义进行消除检测的方法
    private void checkDisappear(){
        //1.判断前后两次点击的按钮序号btnIndex1,brnIndex2
        //如果两次点击同一位置则则不做判断
        if(btnIndex1==btnIndex2)
            return;
        //对应的在pos[][]数组中位置的状态值是否相同
        //1.1找出btnIndex1所对应的N*N个格子上的位置
        int row1 = btnIndex1/N;
        int col1 = btnIndex1%N;

        int row2 = btnIndex2/N;
        int col2 = btnIndex2%N;

        //1.2比较pos[row1][col1] 与 pos[row2][col2]的值
        if (pos[row1][col1]==pos[row2][col2]){      //相同物体
            //判断是否存在通路
            if(hasRouter(row1,col1,row2,col2)){

            //进行消除
            btns[btnIndex1].setVisibility(View.INVISIBLE);
            btns[btnIndex2].setVisibility(View.INVISIBLE);
            pos[row1][col1]=0;
            pos[row2][col2]=0;
            }
        }

    }//end checkDisappear()

    //判断(row1,col1)(row2,col2)之间是否存在通路
    private boolean hasRouter(int row1,int col1,int row2,int col2){
        boolean ret = false;
        //1.判断是否能0拐到达
        if (hasRouter_0(row1,col1,row2,col2))
            return true;
        //2.判断是否能1拐到达
        //3.判断是否能2拐到达

        return ret;
    }//end hasRouter

    //判断是否能0拐到达
    private boolean hasRouter_0(int row1,int col1,int row2,int col2){
        boolean ret = false;
        //1.判断是否横向相连
        if (row1==row2){
            //判断从(row1,col1)横向走到(row2)(col2)是否存在障碍物
            int count=0;    //记录障碍物的个数
            for (int i=col1+1;i<col2;i++){
                if (pos[row1][i]>0){    //遇到障碍物
                    count++;
                    break;
                }
            }
            if (count==0)       //从起点到终点没有遇到任何障碍物
                return true;
        }

        //2.判断是否纵向相连
        if (col1==col2){
            int count=0;
            for (int i=row1+1;i<row2;i++){
                if (pos[i][col1]>0){
                    count++;
                    break;
                }
            }//end for
            if (count==0)
                return true;
        }//end 纵向判断
        return ret;
    }//end hasRouter_0

}
