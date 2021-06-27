# antalyaDutyFree
Bu proje https://antalya.shopdutyfree.com/en/brandboutique/brand/all/ sayfasından başlayarak alt sayfalardaki verileri web scraping yöntemiyle çekerek hem uygulamanın içerisinde göstermeyi hemde veritabanı kaydı tutmayı sağlayacak şekilde geliştirilmiştir.

## Kullanılan teknolojiler
- [Jsoup](https://jsoup.org/) : Web sayfasından verilerin çekilmesi için kullanıldı.
- [Picasso](https://square.github.io/picasso/) : Ürün görsellerinin url üzerinden yüklenmesi için kullanıldı.
- [kotlinx.coroutines](https://github.com/Kotlin/kotlinx.coroutines) : Alt görevler oluşturmak amacıyla kullanıldı.

## Uygulama arayüz ve özellikleri

### [Açılış ekranı(SplashActivity)](app/src/main/java/com/hvk/antalyadutyfree/SplashActivity.kt)
- internet erişim kontrolü yapılarak uygulamanın sorunsuz çalışabilir durumda olması sağlanır.
![](ss/1.png)

- Herhangi bir internet erişimi bulunamazsa uygulama sonlandırılır.
![](ss/2.png)

### [Ana ekran(MainActivity)](app/src/main/java/com/hvk/antalyadutyfree/MainActivity.kt)
- Tüm markaların isim ve ürün sayfası linkleri çekilir.
- Marka isimleri alfabetik olarak kullanıcıya sunulur.
![](ss/4.png)