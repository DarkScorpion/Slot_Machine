package com.as.slotmachine;
//������� �������
import java.util.Random;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences.Editor;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity 
{
	final static int StandartBet = 5;
	final static int MaximalBet = 1000;
	Random rndNum = new Random();
	int bet = 5;
	int balance;
	int imageIdSize,i;
	int FruitCount = 3;
	SharedPreferences sPref;
	TextView tvBet, tvBalance;
	Button btnBetUp,btnBetDown,btnStart;
	//������ ������� �� ��������
	private int [] imageId = 
			{
			R.drawable.fruit00, //������� �����
			R.drawable.fruit01,
			R.drawable.fruit02,
			R.drawable.fruit03,
			R.drawable.fruit04,
			R.drawable.fruit05,
			R.drawable.fruit06,
			R.drawable.fruit07
			};
	
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //���������� UI  � ����������
        imageIdSize = imageId.length;
        btnBetUp = (Button)findViewById(R.id.btnBetUp);
        btnBetDown = (Button)findViewById(R.id.btnBetDown);
        btnStart = (Button)findViewById(R.id.buttonStart);
        tvBet = (TextView)findViewById(R.id.textViewBet);
        tvBalance = (TextView)findViewById(R.id.textViewBalance);
        LoadBalance();
    }
    //������� �� ������ ��������� ������
    public void btnBetUp_Click(View v)
    {
    	if(bet+StandartBet < MaximalBet) //�������� �� ������������ ������
    	{
    		bet+=StandartBet;
    		tvBet.setText("$"+bet);
    	}
    	else ShowMessage("������ �������� ������ ������");
    }
    //������� �� ������ ��������� ������
    public void btnBetDown_Click(View v)
    {	
    	if(bet-StandartBet>0) //�������� �� ����������� ������
		{
			bet-=StandartBet;
			tvBet.setText("$"+bet);
		}
    	else ShowMessage("������ �������� ������ ������");
    }
    //������� �� ������ ������� ��������
    public void btnStart_Click(View v)
    {
    	if (balance-bet >=0 )//�������� ������� � ������� ������
    	{
    		balance-=bet;
    		tvBalance.setText("$"+balance);
    		StartGame();
    	}
    		else 
    		{
    			ShowMessage("� ��� ������������ �������!");
    			bet=5;
        		tvBet.setText("$"+bet);
    		}
    	
    }
    //������� ������� �������� � ��������� ��������
    public void StartGame()
    {
    	int prize;
    	Animation ScaleAnim;
    	int randBuffer[] = new int [FruitCount];
    	ImageView [] ivFruit = {
    			(ImageView)findViewById(R.id.imageView1),
				(ImageView)findViewById(R.id.imageView2),
				(ImageView)findViewById(R.id.imageView3)
				};
    	//�������� ���������� ������ ����� � ����� � ���� ��������.
    	for(i=0;i<FruitCount;i++)
    	{
    		randBuffer[i] = rndNum.nextInt(imageIdSize);
        	ivFruit[i].setImageResource(imageId[randBuffer[i]]);
        	//������ ��������
        	ScaleAnim = AnimationUtils.loadAnimation(this, R.anim.scale_anim);
        	ivFruit[i].startAnimation(ScaleAnim);
    	}
    	//�������� �������� � � ������ ������ ����� ��������� �� �����
    	prize=CheckPrize(randBuffer)*bet;
    	if (prize != 0) 
    	{
    		Music.sound(this, R.raw.slotcoin);
    		ShowMessage("��� �������: $"+prize);
    		AddMoney(prize);
    	}
    }
    //������� �������� ��������
    public int CheckPrize (int imageNum[])
    {
    	if (imageNum[0]==imageNum[1] && imageNum[1]==imageNum[2]) return imageNum[1]*2;
    	if (imageNum[0]==imageNum[1] || imageNum[1]==imageNum[2]) return imageNum[1];
    	if (imageNum[0]==imageNum[2]) return imageNum[0];
    	return 0;
    }
    //������� ������ ��������� �� �����
    public void ShowMessage(String s)
    {
    Toast toast1 = Toast.makeText(getApplicationContext(), 
			   s, Toast.LENGTH_SHORT);
	toast1.setGravity(Gravity.CENTER, 0, 0);
	toast1.show();
    }
    
    //���������� �����
    public void AddMoney(int money)
    {
    	balance+=money;
        tvBalance.setText("$"+balance); 
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) 
    {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    //��������� ������ ��� �������� ��� ������ � ����
    @Override
    protected void onResume() 
    {
       super.onResume();
       Music.play(this, R.raw.main);
    }
    //���������� ������ ��� ����� ��� ������
    @Override
    protected void onPause() 
    {
       super.onPause();
       SaveBalance(balance);
       Music.stop(this);
    }
	
	//�������� ���� ������ �� ����������
	@Override
	public void onBackPressed() 
	{
		new AlertDialog.Builder(this)
		.setTitle("����� �� ����������")
		.setMessage("�� ������������� ������ �����?")
		.setNegativeButton("���", null)
		.setPositiveButton("��", new OnClickListener() 
		{
		public void onClick(DialogInterface arg0, int arg1) 
		{
			finish(); //������� ������ �� ������� Activity
		}
		}).create().show();
	}
	
	
	public void SaveBalance(int balance)
	{
		sPref = getSharedPreferences("FruitMachin_data",MODE_PRIVATE);
		Editor ed = sPref.edit();
		ed.putInt("Balance", balance);
		ed.commit();
	}
	
	public void LoadBalance()
	{
		sPref = getSharedPreferences("FruitMachin_data",MODE_PRIVATE);
		balance =  sPref.getInt("Balance", 150);
		tvBalance.setText("$"+balance);
		
	}
	
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
    	// �������� ��� ���������� ������ ����
        switch (item.getItemId()) 
    	{
        case R.id.menu_AddMoney:
            AddMoney(500);
            Music.sound(this, R.raw.kassa);
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }
}
