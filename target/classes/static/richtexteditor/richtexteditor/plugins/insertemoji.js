RTE_DefaultConfig.plugin_insertemoji = RTE_Plugin_InsertEmoji;

function RTE_Plugin_InsertEmoji() {

    function CharToHTMLCode(ch) {
        if (ch.length > 2)
            return ch;

        if (ch.length == 1 || ch.charCodeAt(1) == 0xfe0f)
            return "&#" + ch.charCodeAt(0) + ";"

        if (ch.charCodeAt(1) == 0xfe0f) {
            return "&#" + ch.charCodeAt(0) + ";"
        }

        var offset = ch.charCodeAt(0) - 0xd83c;
        if (offset < 0 && offset > 3)//not support
            return ch;

        var second = ch.charCodeAt(1) - 0xdc04;
        var f = offset * 0x400 + second + 0x1f004;
        return "&#x" + f.toString(16) + ";"
    }


    var groupnames = ["smileys", "people", "animals", "food", "travel", "activities", "objects", "symbols"]
    var emojistrs = ["😀#grinning face|😃#grinning face with big eyes|😄#grinning face with smiling eyes|😁#beaming face with smiling eyes|😆#grinning squinting face|😅#grinning face with sweat|🤣#rolling on the floor laughing|😂#face with tears of joy|🙂#slightly smiling face|🙃#upside-down face|😉#winking face|😊#smiling face with smiling eyes|😇#smiling face with halo|😍#smiling face with heart-eyes|🤩#star-struck|😘#face blowing a kiss|😗#kissing face|😚#kissing face with closed eyes|😙#kissing face with smiling eyes|😋#face savoring food|😛#face with tongue|😜#winking face with tongue|🤪#zany face|😝#squinting face with tongue|🤑#money-mouth face|🤗#hugging face|🤭#face with hand over mouth|🤫#shushing face|🤔#thinking face|🤐#zipper-mouth face|🤨#face with raised eyebrow|😐#neutral face|😑#expressionless face|😶#face without mouth|😏#smirking face|😒#unamused face|🙄#face with rolling eyes|😬#grimacing face|🤥#lying face|😌#relieved face|😔#pensive face|😪#sleepy face|🤤#drooling face|😴#sleeping face|😷#face with medical mask|🤒#face with thermometer|🤕#face with head-bandage|🤢#nauseated face|🤮#face vomiting|🤧#sneezing face|😵#dizzy face|🤯#exploding head|🤠#cowboy hat face|😎#smiling face with sunglasses|🤓#nerd face|🧐#face with monocle|😕#confused face|😟#worried face|🙁#slightly frowning face|☹️#frowning face|😮#face with open mouth|😯#hushed face|😲#astonished face|😳#flushed face|😦#frowning face with open mouth|😧#anguished face|😨#fearful face|😰#anxious face with sweat|😥#sad but relieved face|😢#crying face|😭#loudly crying face|😱#face screaming in fear|😖#confounded face|😣#persevering face|😞#disappointed face|😓#downcast face with sweat|😩#weary face|😫#tired face|😤#face with steam from nose|😡#pouting face|😠#angry face|🤬#face with symbols on mouth|😈#smiling face with horns|👿#angry face with horns|💀#skull|☠️#skull and crossbones|💩#pile of poo|🤡#clown face|👹#ogre|👺#goblin|👻#ghost|👽#alien|👾#alien monster|🤖#robot|😺#grinning cat|😸#grinning cat with smiling eyes|😹#cat with tears of joy|😻#smiling cat with heart-eyes|😼#cat with wry smile|😽#kissing cat|🙀#weary cat|😿#crying cat|😾#pouting cat|🙈#see-no-evil monkey|🙉#hear-no-evil monkey|🙊#speak-no-evil monkey|💋#kiss mark|💌#love letter|💘#heart with arrow|💝#heart with ribbon|💖#sparkling heart|💗#growing heart|💓#beating heart|💞#revolving hearts|💕#two hearts|💟#heart decoration|❣️#heart exclamation|💔#broken heart|❤️#red heart|🧡#orange heart|💛#yellow heart|💚#green heart|💙#blue heart|💜#purple heart|🖤#black heart|💯#hundred points|💢#anger symbol|💥#collision|💫#dizzy|💦#sweat droplets|💨#dashing away|💣#bomb|💬#speech balloon|💭#thought balloon|💤#zzz", "👋#waving hand|🤚#raised back of hand|✋#raised hand|🖖#vulcan salute|👌#OK hand|✌️#victory hand|🤞#crossed fingers|🤟#love-you gesture|🤘#sign of the horns|🤙#call me hand|👈#backhand index pointing left|👉#backhand index pointing right|👆#backhand index pointing up|🖕#middle finger|👇#backhand index pointing down|☝️#index pointing up|👍#thumbs up|👎#thumbs down|✊#raised fist|👊#oncoming fist|🤛#left-facing fist|🤜#right-facing fist|👏#clapping hands|🙌#raising hands|👐#open hands|🤲#palms up together|🤝#handshake|🙏#folded hands|✍️#writing hand|💅#nail polish|🤳#selfie|💪#flexed biceps|👂#ear|👃#nose|🧠#brain|👀#eyes|👅#tongue|👄#mouth|👶#baby|🧒#child|👦#boy|👧#girl|🧑#person|👱#person with blond hair|👨#man|🧔#man with beard|👩#woman|🧓#older person|👴#old man|👵#old woman|🙍#person frowning|🙎#person pouting|🙅#person gesturing NO|🙆#person gesturing OK|💁#person tipping hand|🙋#person raising hand|🙇#person bowing|🤦#person facepalming|🤷#person shrugging|👮#police officer|💂#guard|👷#construction worker|🤴#prince|👸#princess|👳#person wearing turban|👲#person with skullcap|🧕#woman with headscarf|🤵#person in tuxedo|👰#person with veil|🤰#pregnant woman|🤱#breast-feeding|👼#baby angel|🎅#Santa Claus|🤶#Mrs. Claus|🧙#mage|🧚#fairy|🧛#vampire|🧜#merperson|🧝#elf|🧞#genie|🧟#zombie|💆#person getting massage|💇#person getting haircut|🚶#person walking|🏃#person running|💃#woman dancing|🕺#man dancing|👯#people with bunny ears|🧖#person in steamy room|🧗#person climbing|🤺#person fencing|🏇#horse racing|⛷️#skier|🏂#snowboarder|🏄#person surfing|🚣#person rowing boat|🏊#person swimming|⛹️#person bouncing ball|🚴#person biking|🚵#person mountain biking|🤸#person cartwheeling|🤼#people wrestling|🤽#person playing water polo|🤾#person playing handball|🤹#person juggling|🧘#person in lotus position|🛀#person taking bath|🛌#person in bed|👭#women holding hands|👫#woman and man holding hands|👬#men holding hands|💏#kiss|💑#couple with heart|👪#family|👤#bust in silhouette|👥#busts in silhouette|👣#footprints", "🐵#monkey face|🐒#monkey|🦍#gorilla|🐶#dog face|🐕#dog|🐩#poodle|🐺#wolf|🦊#fox|🐱#cat face|🐈#cat|🦁#lion|🐯#tiger face|🐅#tiger|🐆#leopard|🐴#horse face|🐎#horse|🦄#unicorn|🦓#zebra|🦌#deer|🐮#cow face|🐂#ox|🐃#water buffalo|🐄#cow|🐷#pig face|🐖#pig|🐗#boar|🐽#pig nose|🐏#ram|🐑#ewe|🐐#goat|🐪#camel|🐫#two-hump camel|🦒#giraffe|🐘#elephant|🦏#rhinoceros|🐭#mouse face|🐁#mouse|🐀#rat|🐹#hamster|🐰#rabbit face|🐇#rabbit|🦔#hedgehog|🦇#bat|🐻#bear|🐨#koala|🐼#panda|🐾#paw prints|🦃#turkey|🐔#chicken|🐓#rooster|🐣#hatching chick|🐤#baby chick|🐥#front-facing baby chick|🐦#bird|🐧#penguin|🦅#eagle|🦆#duck|🦉#owl|🐸#frog|🐊#crocodile|🐢#turtle|🦎#lizard|🐍#snake|🐲#dragon face|🐉#dragon|🦕#sauropod|🦖#T-Rex|🐳#spouting whale|🐋#whale|🐬#dolphin|🐟#fish|🐠#tropical fish|🐡#blowfish|🦈#shark|🐙#octopus|🐚#spiral shell|🐌#snail|🦋#butterfly|🐛#bug|🐜#ant|🐝#honeybee|🐞#lady beetle|🦗#cricket|🦂#scorpion|💐#bouquet|🌸#cherry blossom|💮#white flower|🌹#rose|🥀#wilted flower|🌺#hibiscus|🌻#sunflower|🌼#blossom|🌷#tulip|🌱#seedling|🌲#evergreen tree|🌳#deciduous tree|🌴#palm tree|🌵#cactus|🌾#sheaf of rice|🌿#herb|☘️#shamrock|🍀#four leaf clover|🍁#maple leaf|🍂#fallen leaf|🍃#leaf fluttering in wind", "🍇#grapes|🍈#melon|🍉#watermelon|🍊#tangerine|🍋#lemon|🍌#banana|🍍#pineapple|🍎#red apple|🍏#green apple|🍐#pear|🍑#peach|🍒#cherries|🍓#strawberry|🥝#kiwi fruit|🍅#tomato|🥥#coconut|🥑#avocado|🍆#eggplant|🥔#potato|🥕#carrot|🌽#ear of corn|🥒#cucumber|🥦#broccoli|🍄#mushroom|🥜#peanuts|🌰#chestnut|🍞#bread|🥐#croissant|🥖#baguette bread|🥨#pretzel|🥞#pancakes|🧀#cheese wedge|🍖#meat on bone|🍗#poultry leg|🥩#cut of meat|🥓#bacon|🍔#hamburger|🍟#french fries|🍕#pizza|🌭#hot dog|🥪#sandwich|🌮#taco|🌯#burrito|🥙#stuffed flatbread|🥚#egg|🍳#cooking|🥘#shallow pan of food|🍲#pot of food|🥣#bowl with spoon|🥗#green salad|🍿#popcorn|🥫#canned food|🍱#bento box|🍘#rice cracker|🍙#rice ball|🍚#cooked rice|🍛#curry rice|🍜#steaming bowl|🍝#spaghetti|🍠#roasted sweet potato|🍢#oden|🍣#sushi|🍤#fried shrimp|🍥#fish cake with swirl|🍡#dango|🥟#dumpling|🥠#fortune cookie|🥡#takeout box|🦀#crab|🦐#shrimp|🦑#squid|🍦#soft ice cream|🍧#shaved ice|🍨#ice cream|🍩#doughnut|🍪#cookie|🎂#birthday cake|🍰#shortcake|🥧#pie|🍫#chocolate bar|🍬#candy|🍭#lollipop|🍮#custard|🍯#honey pot|🍼#baby bottle|🥛#glass of milk|☕#hot beverage|🍵#teacup without handle|🍶#sake|🍾#bottle with popping cork|🍷#wine glass|🍸#cocktail glass|🍹#tropical drink|🍺#beer mug|🍻#clinking beer mugs|🥂#clinking glasses|🥃#tumbler glass|🥤#cup with straw|🥢#chopsticks|🍴#fork and knife|🥄#spoon|🔪#kitchen knife|🏺#amphora", "🌍#globe showing Europe-Africa|🌎#globe showing Americas|🌏#globe showing Asia-Australia|🌐#globe with meridians|🗾#map of Japan|⛰️#mountain|🌋#volcano|🗻#mount fuji|🏠#house|🏡#house with garden|🏢#office building|🏣#Japanese post office|🏤#post office|🏥#hospital|🏦#bank|🏨#hotel|🏩#love hotel|🏪#convenience store|🏫#school|🏬#department store|🏭#factory|🏯#Japanese castle|🏰#castle|💒#wedding|🗼#Tokyo tower|🗽#Statue of Liberty|⛪#church|🕌#mosque|🕍#synagogue|⛩️#shinto shrine|🕋#kaaba|⛲#fountain|⛺#tent|🌁#foggy|🌃#night with stars|🌄#sunrise over mountains|🌅#sunrise|🌆#cityscape at dusk|🌇#sunset|🌉#bridge at night|♨️#hot springs|🎠#carousel horse|🎡#ferris wheel|🎢#roller coaster|💈#barber pole|🎪#circus tent|🚂#locomotive|🚃#railway car|🚄#high-speed train|🚅#bullet train|🚆#train|🚇#metro|🚈#light rail|🚉#station|🚊#tram|🚝#monorail|🚞#mountain railway|🚋#tram car|🚌#bus|🚍#oncoming bus|🚎#trolleybus|🚐#minibus|🚑#ambulance|🚒#fire engine|🚓#police car|🚔#oncoming police car|🚕#taxi|🚖#oncoming taxi|🚗#automobile|🚘#oncoming automobile|🚙#sport utility vehicle|🚚#delivery truck|🚛#articulated lorry|🚜#tractor|🛵#motor scooter|🚲#bicycle|🛴#kick scooter|🚏#bus stop|⛽#fuel pump|🚨#police car light|🚥#horizontal traffic light|🚦#vertical traffic light|🛑#stop sign|🚧#construction|⚓#anchor|⛵#sailboat|🛶#canoe|🚤#speedboat|⛴️#ferry|🚢#ship|✈️#airplane|🛫#airplane departure|🛬#airplane arrival|💺#seat|🚁#helicopter|🚟#suspension railway|🚠#mountain cableway|🚡#aerial tramway|🚀#rocket|🛸#flying saucer|⌛#hourglass done|⏳#hourglass not done|⌚#watch|⏰#alarm clock|⏱️#stopwatch|⏲️#timer clock|🕛#twelve o’clock|🕧#twelve-thirty|🕐#one o’clock|🕜#one-thirty|🕑#two o’clock|🕝#two-thirty|🕒#three o’clock|🕞#three-thirty|🕓#four o’clock|🕟#four-thirty|🕔#five o’clock|🕠#five-thirty|🕕#six o’clock|🕡#six-thirty|🕖#seven o’clock|🕢#seven-thirty|🕗#eight o’clock|🕣#eight-thirty|🕘#nine o’clock|🕤#nine-thirty|🕙#ten o’clock|🕥#ten-thirty|🕚#eleven o’clock|🕦#eleven-thirty|🌑#new moon|🌒#waxing crescent moon|🌓#first quarter moon|🌔#waxing gibbous moon|🌕#full moon|🌖#waning gibbous moon|🌗#last quarter moon|🌘#waning crescent moon|🌙#crescent moon|🌚#new moon face|🌛#first quarter moon face|🌜#last quarter moon face|☀️#sun|🌝#full moon face|🌞#sun with face|⭐#star|🌟#glowing star|🌠#shooting star|🌌#milky way|☁️#cloud|⛅#sun behind cloud|⛈️#cloud with lightning and rain|🌀#cyclone|🌈#rainbow|🌂#closed umbrella|☂️#umbrella|☔#umbrella with rain drops|⛱️#umbrella on ground|⚡#high voltage|❄️#snowflake|☃️#snowman|⛄#snowman without snow|☄️#comet|🔥#fire|💧#droplet|🌊#water wave", "🎃#jack-o-lantern|🎄#Christmas tree|🎆#fireworks|🎇#sparkler|✨#sparkles|🎈#balloon|🎉#party popper|🎊#confetti ball|🎋#tanabata tree|🎍#pine decoration|🎎#Japanese dolls|🎏#carp streamer|🎐#wind chime|🎑#moon viewing ceremony|🎀#ribbon|🎁#wrapped gift|🎫#ticket|🏆#trophy|🏅#sports medal|🥇#1st place medal|🥈#2nd place medal|🥉#3rd place medal|⚽#soccer ball|⚾#baseball|🏀#basketball|🏐#volleyball|🏈#american football|🏉#rugby football|🎾#tennis|🎳#bowling|🏏#cricket game|🏑#field hockey|🏒#ice hockey|🏓#ping pong|🏸#badminton|🥊#boxing glove|🥋#martial arts uniform|🥅#goal net|⛳#flag in hole|⛸️#ice skate|🎣#fishing pole|🎽#running shirt|🎿#skis|🛷#sled|🥌#curling stone|🎯#direct hit|🎱#pool 8 ball|🔮#crystal ball|🎮#video game|🎰#slot machine|🎲#game die|♠️#spade suit|♥️#heart suit|♦️#diamond suit|♣️#club suit|🃏#joker|🀄#mahjong red dragon|🎴#flower playing cards|🎭#performing arts|🎨#artist palette", "👓#glasses|👔#necktie|👕#t-shirt|👖#jeans|🧣#scarf|🧤#gloves|🧥#coat|🧦#socks|👗#dress|👘#kimono|👙#bikini|👚#woman’s clothes|👛#purse|👜#handbag|👝#clutch bag|🎒#backpack|👞#man’s shoe|👟#running shoe|👠#high-heeled shoe|👡#woman’s sandal|👢#woman’s boot|👑#crown|👒#woman’s hat|🎩#top hat|🎓#graduation cap|🧢#billed cap|⛑️#rescue worker’s helmet|📿#prayer beads|💄#lipstick|💍#ring|💎#gem stone|🔇#muted speaker|🔈#speaker low volume|🔉#speaker medium volume|🔊#speaker high volume|📢#loudspeaker|📣#megaphone|📯#postal horn|🔔#bell|🔕#bell with slash|🎼#musical score|🎵#musical note|🎶#musical notes|🎤#microphone|🎧#headphone|📻#radio|🎷#saxophone|🎸#guitar|🎹#musical keyboard|🎺#trumpet|🎻#violin|🥁#drum|📱#mobile phone|📲#mobile phone with arrow|☎️#telephone|📞#telephone receiver|📟#pager|📠#fax machine|🔋#battery|🔌#electric plug|💻#laptop|⌨️#keyboard|💽#computer disk|💾#floppy disk|💿#optical disk|📀#dvd|🎥#movie camera|🎬#clapper board|📺#television|📷#camera|📸#camera with flash|📹#video camera|📼#videocassette|🔍#magnifying glass tilted left|🔎#magnifying glass tilted right|💡#light bulb|🔦#flashlight|🏮#red paper lantern|📔#notebook with decorative cover|📕#closed book|📖#open book|📗#green book|📘#blue book|📙#orange book|📚#books|📓#notebook|📒#ledger|📃#page with curl|📜#scroll|📄#page facing up|📰#newspaper|📑#bookmark tabs|🔖#bookmark|💰#money bag|💴#yen banknote|💵#dollar banknote|💶#euro banknote|💷#pound banknote|💸#money with wings|💳#credit card|💹#chart increasing with yen|✉️#envelope|📧#e-mail|📨#incoming envelope|📩#envelope with arrow|📤#outbox tray|📥#inbox tray|📦#package|📫#closed mailbox with raised flag|📪#closed mailbox with lowered flag|📬#open mailbox with raised flag|📭#open mailbox with lowered flag|📮#postbox|✏️#pencil|✒️#black nib|📝#memo|💼#briefcase|📁#file folder|📂#open file folder|📅#calendar|📆#tear-off calendar|📇#card index|📈#chart increasing|📉#chart decreasing|📊#bar chart|📋#clipboard|📌#pushpin|📍#round pushpin|📎#paperclip|📏#straight ruler|📐#triangular ruler|✂️#scissors|🔒#locked|🔓#unlocked|🔏#locked with pen|🔐#locked with key|🔑#key|🔨#hammer|⛏️#pick|⚒️#hammer and pick|⚔️#crossed swords|🔫#pistol|🏹#bow and arrow|🔧#wrench|🔩#nut and bolt|⚙️#gear|⚖️#balance scale|🔗#link|⛓️#chains|⚗️#alembic|🔬#microscope|🔭#telescope|📡#satellite antenna|💉#syringe|💊#pill|🚪#door|🚽#toilet|🚿#shower|🛁#bathtub|🛒#shopping cart|🚬#cigarette|⚰️#coffin|⚱️#funeral urn|🗿#moai", "🏧#ATM sign|🚮#litter in bin sign|🚰#potable water|♿#wheelchair symbol|🚹#men’s room|🚺#women’s room|🚻#restroom|🚼#baby symbol|🚾#water closet|🛂#passport control|🛃#customs|🛄#baggage claim|🛅#left luggage|⚠️#warning|🚸#children crossing|⛔#no entry|🚫#prohibited|🚳#no bicycles|🚭#no smoking|🚯#no littering|🚱#non-potable water|🚷#no pedestrians|📵#no mobile phones|🔞#no one under eighteen|☢️#radioactive|☣️#biohazard|⬆️#up arrow|↗️#up-right arrow|➡️#right arrow|↘️#down-right arrow|⬇️#down arrow|↙️#down-left arrow|⬅️#left arrow|↖️#up-left arrow|↕️#up-down arrow|↔️#left-right arrow|↩️#right arrow curving left|↪️#left arrow curving right|⤴️#right arrow curving up|⤵️#right arrow curving down|🔃#clockwise vertical arrows|🔄#counterclockwise arrows button|🔙#BACK arrow|🔚#END arrow|🔛#ON! arrow|🔜#SOON arrow|🔝#TOP arrow|🛐#place of worship|⚛️#atom symbol|✡️#star of David|☸️#wheel of dharma|☯️#yin yang|✝️#latin cross|☦️#orthodox cross|☪️#star and crescent|☮️#peace symbol|🕎#menorah|🔯#dotted six-pointed star|♈#Aries|♉#Taurus|♊#Gemini|♋#Cancer|♌#Leo|♍#Virgo|♎#Libra|♏#Scorpio|♐#Sagittarius|♑#Capricorn|♒#Aquarius|♓#Pisces|⛎#Ophiuchus|🔀#shuffle tracks button|🔁#repeat button|🔂#repeat single button|▶️#play button|⏩#fast-forward button|⏭️#next track button|⏯️#play or pause button|◀️#reverse button|⏪#fast reverse button|⏮️#last track button|🔼#upwards button|⏫#fast up button|🔽#downwards button|⏬#fast down button|⏸️#pause button|⏹️#stop button|⏺️#record button|⏏️#eject button|🎦#cinema|🔅#dim button|🔆#bright button|📶#antenna bars|📳#vibration mode|📴#mobile phone off|♀️#female sign|♂️#male sign|✖️#multiply|➕#plus|➖#minus|➗#divide|‼️#double exclamation mark|⁉️#exclamation question mark|❓#question mark|❔#white question mark|❕#white exclamation mark|❗#exclamation mark|〰️#wavy dash|💱#currency exchange|💲#heavy dollar sign|⚕️#medical symbol|♻️#recycling symbol|⚜️#fleur-de-lis|🔱#trident emblem|📛#name badge|🔰#Japanese symbol for beginner|⭕#hollow red circle|✅#check mark button|☑️#check box with check|✔️#check mark|❌#cross mark|❎#cross mark button|➰#curly loop|➿#double curly loop|〽️#part alternation mark|✳️#eight-spoked asterisk|✴️#eight-pointed star|❇️#sparkle|©️#copyright|®️#registered|™️#trade mark|🔟#keycap: 10|🔠#input latin uppercase|🔡#input latin lowercase|🔢#input numbers|🔣#input symbols|🔤#input latin letters|🆎#AB button (blood type)|🆑#CL button|🆒#COOL button|🆓#FREE button|ℹ️#information|🆔#ID button|Ⓜ️#circled M|🆕#NEW button|🆖#NG button|🆗#OK button|🆘#SOS button|🆙#UP! button|🆚#VS button|🈁#Japanese “here” button|🈶#Japanese “not free of charge” button|🈯#Japanese “reserved” button|🉐#Japanese “bargain” button|🈹#Japanese “discount” button|🈚#Japanese “free of charge” button|🈲#Japanese “prohibited” button|🉑#Japanese “acceptable” button|🈸#Japanese “application” button|🈴#Japanese “passing grade” button|🈳#Japanese “vacancy” button|㊗️#Japanese “congratulations” button|㊙️#Japanese “secret” button|🈺#Japanese “open for business” button|🈵#Japanese “no vacancy” button|🔴#red circle|🔵#blue circle|⚫#black circle|⚪#white circle|⬛#black large square|⬜#white large square|◼️#black medium square|◻️#white medium square|◾#black medium-small square|◽#white medium-small square|▪️#black small square|▫️#white small square|🔶#large orange diamond|🔷#large blue diamond|🔸#small orange diamond|🔹#small blue diamond|🔺#red triangle pointed up|🔻#red triangle pointed down|💠#diamond with a dot|🔘#radio button|🔳#white square button|🔲#black square button"]
    var emojidata = null;

    function MakeEmojiData() {
        if (emojidata)
            return;
        emojidata = [];
        for (var groupindex = 0; groupindex < groupnames.length; groupindex++) {
            var emojiitems = [];
            var emojigroup = {index: groupindex, name: groupnames[groupindex], items: emojiitems};
            var emojiarr = emojistrs[groupindex].split('|');
            for (var ei = 0; ei < emojiarr.length; ei++) {
                var emojistr = emojiarr[ei];
                var pair = emojistr.split('#')
                emojiitems.push({emoji: pair[0], keyword: pair[1]});
            }
            emojidata.push(emojigroup);
        }
        //console.log(emojidata);
    }

    var obj = this;

    var config, editor;

    obj.PluginName = "InsertEmoji";

    obj.InitConfig = function (argconfig) {
        config = argconfig;
    }
    obj.InitEditor = function (argeditor) {
        editor = argeditor;

        editor.toolbarFactoryMap["insertemoji"] = function (cmd) {
            return editor.createToolbarItemDropDownPanel(cmd, function (panel) {

                MakeEmojiData()

                panel.style.width = "360px";
                panel.style.height = "420px";
                panel.style.display = "flex";
                panel.style.flexDirection = "column";

                panel.onclick = function (e) {
                    if (e.target.nodeName == "GSPAN") {
                        editor.closeCurrentPopup();
                        var htmlcode = e.target.getAttribute("htmlcode");
                        console.log("insert emoji", htmlcode)
                        editor.insertHTML(htmlcode);
                        editor.collapse(false);
                        editor.focus();
                    }
                }

                var selecteditem = null;
                var toselectitem = null;

                function clear_selecteditem() {
                    if (selecteditem != null) {
                        selecteditem.style.backgroundColor = "";
                        selecteditem = null;
                    }
                }

                function set_selecteditem() {
                    clear_selecteditem();
                    selecteditem = toselectitem;
                    selecteditem.style.backgroundColor = "#e6e6e6";
                    toselectitem = null;
                }

                var tid_sel = 0;

                panel.onmouseover = function (e) {
                    for (var node = e.target; node != panel; node = node.parentNode) {
                        if (node.nodeName == "GITEM") {
                            if (node == toselectitem)
                                return;
                            toselectitem = node;
                            clearTimeout(tid_sel);
                            tid_sel = setTimeout(set_selecteditem, 10);
                            return;
                        }
                    }
                }
                panel.onmouseout = function () {
                    clearTimeout(tid_sel);
                    tid_sel = setTimeout(clear_selecteditem, 10)
                }

                var searchbar = __Append(panel, "label", "margin:5px;position:relative;");
                searchbar.setAttribute("id", "emojis_searchbar");
                var searchbox = __Append(searchbar, "input", "width:100%;padding:5px 20px;border:solid 1px #ccc;border-radius:5px;");
                searchbox.setAttribute("placeholder", editor.getLangText("searchemojis"));

                var tid_key = 0;
                searchbox.onchange = searchbox.onkeyup = searchbox.onkeypress = searchbox.onpaste = function () {
                    clearTimeout(tid_key);
                    tid_key = setTimeout(show_result, 100);
                }

                function show_result() {
                    var keyword = searchbox.value.trim().toLowerCase();
                    if (!keyword) {
                        tabpanel.style.display =
                            grouppanel.style.display = "";
                        resultpanel.style.display = "none";
                        return;
                    }

                    tabpanel.style.display =
                        grouppanel.style.display = "none";
                    resultpanel.style.display = "flex";
                    resultpanel.innerHTML = "";

                    var resultline = __Append(resultpanel, "div", "width:100%;padding:3px;margin-top:5px;color:darkblue;text-align:center;");

                    var itemindex = 0;


                    for (var gi = 0; gi < emojidata.length; gi++) {
                        var group = emojidata[gi];
                        for (var ii = 0; ii < group.items.length; ii++) {
                            var item = group.items[ii];

                            if (!item.keyword || item.keyword.indexOf(keyword) == -1)
                                continue;

                            itemindex++;

                            //if (itemindex > 20)break;
                            var gitem = __Append(resultpanel, "gitem", "width:32px;height:32px;margin:2px", "rte-flex-column-center")
                            var gspan = __Append(gitem, "gspan", "");
                            var htmlcode = CharToHTMLCode(item.emoji);
                            gspan.setAttribute("title", item.emoji + " " + item.keyword)
                            gspan.setAttribute("htmlcode", htmlcode)
                            gspan.innerHTML = htmlcode;
                        }
                    }

                    resultline.innerText = itemindex + " items";

                }

                searchbox.focus();

                panel.setAttribute("id", "emoji-picker");

                var tabpanel = __Append(panel, "div");

                var resultpanel = __Append(panel, "div", "display:none;flex-direction:row;flex-wrap:wrap;overflow-y:scroll;padding-bottom:55px");

                var grouppanel = __Append(panel, "div", "overflow-y:scroll;padding-bottom:55px;flex:999");

                var groupdivs = [];

                for (var gi = 0; gi < emojidata.length; gi++) {
                    var group = emojidata[gi];
                    var gdiv = __Append(grouppanel, "div", "padding:3px;margin-top:5px;color:darkblue;");
                    groupdivs.push(gdiv);
                    gdiv.innerText = group.name[0].toUpperCase() + group.name.substring(1);

                    gdiv = __Append(grouppanel, "div", "display:flex;flex-direction:row;flex-wrap:wrap;");

                    for (var itemindex = 0; itemindex < group.items.length; itemindex++) {
                        var item = group.items[itemindex];
                        //if (itemindex > 20)break;
                        var gitem = __Append(gdiv, "gitem", "width:32px;height:32px;margin:2px", "rte-flex-column-center")
                        var gspan = __Append(gitem, "gspan", "");
                        var htmlcode = CharToHTMLCode(item.emoji);
                        gspan.setAttribute("title", item.emoji + " " + item.keyword)
                        gspan.setAttribute("htmlcode", htmlcode)
                        gspan.innerHTML = htmlcode;
                    }
                }

                var tabui = __Append(tabpanel, "rte-tabui");
                tabui.setAttribute("id", "emoji-picker");
                var tabuitoolbar = __Append(tabui, "rte-tabui-toolbar");
                var tabuibtns = [];

                function CreateTabBtn(group) {
                    var btn = __Append(tabuitoolbar, "rte-tabui-toolbar-button", "width:32px;text-align:center;margin:4px")
                    tabuibtns.push(btn);
                    btn.setAttribute("title", group.name);
                    btn.innerHTML = group.items[0].emoji
                    btn.onclick = function () {
                        grouppanel.scrollTop = groupdivs[group.index].getBoundingClientRect().top - grouppanel.getBoundingClientRect().top + grouppanel.scrollTop;
                        grouppanel.onscroll();
                    }
                    btn.group = group;
                }

                for (var gi = 0; gi < emojidata.length; gi++) {
                    var group = emojidata[gi];
                    CreateTabBtn(group)
                }

                var lastactivebtn = null;
                grouppanel.onscroll = function () {
                    var ptop = grouppanel.getBoundingClientRect().top;
                    console.log(ptop);
                    if (lastactivebtn) lastactivebtn.className = "";
                    for (var bi = 0; bi < tabuibtns.length; bi++) {
                        var btn = tabuibtns[bi];
                        var gdiv = groupdivs[btn.group.index];
                        if (gdiv.getBoundingClientRect().top > ptop) {
                            lastactivebtn = tabuibtns[btn.group.index - 1] || btn;
                            lastactivebtn.className = "rte-ui-active";
                            return;
                        }
                    }

                    lastactivebtn = tabuibtns[tabuibtns.length - 1];
                    lastactivebtn.className = "rte-ui-active";
                }
                grouppanel.onscroll();


            })
        };

    }

    function __Append(parent, tagname, csstext, cssclass) {
        var tag = parent.ownerDocument.createElement(tagname);
        if (csstext) tag.style.cssText = csstext;
        if (cssclass) tag.className = cssclass;
        parent.appendChild(tag);
        return tag;
    }


}




