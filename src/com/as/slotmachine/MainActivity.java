package com.as.slotmachine;
//Игровой автомат
import java.util.Random;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
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
	int bet=5;
	int balance=100;
	int imageIDSize,i,n=3;
	Button btnBetUp,btnBetDown,btnStart;
	TextView tvBet, tvBalance;
	//Массив сссылок на картинки
	private int [] imageId = {
			R.drawable.fruit00, //Нулевая фишка
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
        //Связывание UI  и переменных
        imageIDSize = imageId.length;
        btnBetUp = (Button)findViewById(R.id.btnBetUp);
        btnBetDown = (Button)findViewById(R.id.btnBetDown);
        btnStart = (Button)findViewById(R.id.buttonStart);
        tvBet = (TextView)findViewById(R.id.textViewBet);
        tvBalance = (TextView)findViewById(R.id.textViewBalance);
    }
    //Нажатие на кнопку повышения ставки
    public void btnBetUp_Click(View v)
    {
    	if(bet<MaximalBet) //Проверка на максимальную ставку
    	{
    		bet+=StandartBet;
    		tvBet.setText("$"+bet);
    	}
    	else ShowMessage("Дальше повысить ставку нельзя");
    }
    //Нажатие на кнопку понижения ставки
    public void btnBetDown_Click(View v)
    {	
    	if(bet-StandartBet>0) //Проверка на минимальную ставку
		{
			bet-=StandartBet;
			tvBet.setText("$"+bet);
		}
    	else ShowMessage("Дальше понизить ставку нельзя");
    }
    //Нажатие на кнопку запуска автомата
    public void btnStart_Click(View v)
    {
    	if (balance-bet >=0 )//Проверка баланса с вычетом ставки
    	{
    		balance-=bet;
    		tvBalance.setText("$"+balance);
    		StartGame();
    	}
    		else 
    		{
    			ShowMessage("У вас недостаточно средств!");
    			bet=5;
        		tvBet.setText("$"+bet);
    		}
    	
    }
    //Функция запуска автомата с проверкой выигрыша
    public void StartGame()
    {
    	Animation ScaleAnim;
    	int prize;
    	int randBuffer[] = new int [n];
    	ImageView [] ivFruit = {
    			(ImageView)findViewById(R.id.imageView1),
				(ImageView)findViewById(R.id.imageView2),
				(ImageView)findViewById(R.id.imageView3)
				};
    	//Создание случайного набора чисел и вывод в виде картинок.
    	for(i=0;i<n;i++)
    	{
    		randBuffer[i] = rndNum.nextInt(imageIDSize);
        	ivFruit[i].setImageResource(imageId[randBuffer[i]]);
        	//Запуск анимации
        	ScaleAnim = AnimationUtils.loadAnimation(this, R.anim.scale_anim);
        	ivFruit[i].startAnimation(ScaleAnim);
    	}
    	//Проверка выигрыша и в случае победы вывод сообщения на экран
    	prize=CheckPrize(randBuffer)*bet;
    	if (prize != 0) 
    	{
    		Music.sound(this, R.raw.slotcoin);
    		ShowMessage("Ваш выигрыш: $"+prize);
    		AddMoney(prize);
    	}
    }
    //Функция проверки выигрыша
    public int CheckPrize (int imageNum[])
    {
    	if (imageNum[0]==imageNum[1] && imageNum[1]==imageNum[2]) return imageNum[1]*2;
    	if (imageNum[0]==imageNum[1] || imageNum[1]==imageNum[2]) return imageNum[1];
    	if (imageNum[0]==imageNum[2]) return imageNum[0];
    	return 0;
    }
    //Функция вывода сообещния на экран
    public void ShowMessage(String s)
    {
    Toast toast1 = Toast.makeText(getApplicationContext(), 
			   s, Toast.LENGTH_SHORT);
	toast1.setGravity(Gravity.CENTER, 0, 0);
	toast1.show();
    }
    
    //Добавление денег
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

    //Включение музыки при возврате или заходе в игру
    @Override
    protected void onResume() 
    {
       super.onResume();
       Music.play(this, R.raw.main);
    }
    //Отключение музыки при паузе или выходе
    @Override
    protected void onPause() 
    {
       super.onPause();
       Music.stop(this);
    }
	
	
	//Создание окна выхода из приложения
	@Override
	public void onBackPressed() 
	{
		new AlertDialog.Builder(this)
		.setTitle("Выход из приложения")
		.setMessage("Вы действительно хотите выйти?")
		.setNegativeButton("НЕТ", null)
		.setPositiveButton("ДА", new OnClickListener() 
		{
		public void onClick(DialogInterface arg0, int arg1) 
		{
			finish(); //Очищает память от данного Activity
		}
		}).create().show();
	}
	
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
    	// Операции для выбранного пункта меню
        switch (item.getItemId()) 
    	{
        case R.id.menu_AddMoney:
            AddMoney(500);
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }
}
