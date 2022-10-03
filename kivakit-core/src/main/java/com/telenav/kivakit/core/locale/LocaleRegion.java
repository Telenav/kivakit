////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
// © 2011-2021 Telenav, Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.locale;

import com.telenav.kivakit.annotations.code.ApiQuality;
import com.telenav.kivakit.core.language.reflection.property.KivaKitExcludeProperty;

import static com.telenav.kivakit.annotations.code.ApiStability.API_STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NONE;

/**
 * This class defines regions by name and relevant ISO codes. Provides the ISO region code, which can be represented by:
 * a two-letter code (alpha-2) is recommended as the general purpose code. Also provided is a three-letter code
 * (alpha-3) which is more closely related to the region name, and an ISO number. More detail please refer to
 * https://www.iso.org/iso/country_codes.htm.
 *
 * @author Junwei
 * @author jonathanl (shibo)
 * @version 1.0.0 2012-7-12
 */
@SuppressWarnings({ "JavadocLinkAsPlainText", "unused" })
@ApiQuality(stability = API_STABLE_EXTENSIBLE,
            testing = TESTING_NONE,
            documentation = DOCUMENTATION_COMPLETE)
public class LocaleRegion
{
    public static final LocaleRegion WORLD = new LocaleRegion("Earth", "EA", "EAR", -1);

    public static final LocaleRegion AFGHANISTAN = new LocaleRegion("Afghanistan", "AF", "AFG", 4);

    public static final LocaleRegion ALAND = new LocaleRegion("Aland", "AX", "ALA", 248);

    public static final LocaleRegion ALBANIA = new LocaleRegion("Albania", "AL", "ALB", 8);

    public static final LocaleRegion ALGERIA = new LocaleRegion("Algeria", "DZ", "DZA", 12);

    public static final LocaleRegion AMERICAN_SAMOA = new LocaleRegion("American Samoa", "AS", "ASM", 16);

    public static final LocaleRegion ANDORRA = new LocaleRegion("Andorra", "AD", "AND", 20);

    public static final LocaleRegion ANGOLA = new LocaleRegion("Angola", "AO", "AGO", 24);

    public static final LocaleRegion ANGUILLA = new LocaleRegion("Anguilla", "AI", "AIA", 660);

    public static final LocaleRegion ANTIGUA_AND_BARBUDA = new LocaleRegion("Antigua And Barbuda", "AG", "ATG", 28);

    public static final LocaleRegion ARGENTINA = new LocaleRegion("Argentina", "AR", "ARG", 32);

    public static final LocaleRegion ARMENIA = new LocaleRegion("Armenia", "AM", "ARM", 51);

    public static final LocaleRegion ARUBA = new LocaleRegion("Aruba", "AW", "ABW", 533);

    public static final LocaleRegion AUSTRALIA = new LocaleRegion("Australia", "AU", "AUS", 36);

    public static final LocaleRegion AUSTRIA = new LocaleRegion("Austria", "AT", "AUT", 40);

    public static final LocaleRegion AZERBAIJAN = new LocaleRegion("Azerbaijan", "AZ", "AZE", 31);

    public static final LocaleRegion BAHAMAS = new LocaleRegion("Bahamas", "BS", "BHS", 44);

    public static final LocaleRegion BAHRAIN = new LocaleRegion("Bahrain", "BH", "BHR", 48);

    public static final LocaleRegion BANGLADESH = new LocaleRegion("Bangladesh", "BD", "BGD", 50);

    public static final LocaleRegion BARBADOS = new LocaleRegion("Barbados", "BB", "BRB", 52);

    public static final LocaleRegion BELARUS = new LocaleRegion("Belarus", "BY", "BLR", 112);

    public static final LocaleRegion BELGIUM = new LocaleRegion("Belgium", "BE", "BEL", 56);

    public static final LocaleRegion BELIZE = new LocaleRegion("Belize", "BZ", "BLZ", 84);

    public static final LocaleRegion BENIN = new LocaleRegion("Benin", "BJ", "BEN", 204);

    public static final LocaleRegion BERMUDA = new LocaleRegion("Bermuda", "BM", "BMU", 60);

    public static final LocaleRegion BHUTAN = new LocaleRegion("Bhutan", "BT", "BTN", 64);

    public static final LocaleRegion BOLIVIA = new LocaleRegion("Bolivia", "BO", "BOL", 68);

    public static final LocaleRegion BOSNIA_AND_HERZEGOVINA = new LocaleRegion("Bosnia And Herzegovina", "BA", "BIH", 70);

    public static final LocaleRegion BOTSWANA = new LocaleRegion("Botswana", "BW", "BWA", 72);

    public static final LocaleRegion BRAZIL = new LocaleRegion("Brazil", "BR", "BRA", 76);

    public static final LocaleRegion BRITISH_INDIAN_OCEAN_TERRITORY = new LocaleRegion("British Indian Ocean Territory", "IO", "IOT", 86);

    @SuppressWarnings("SpellCheckingInspection")
    public static final LocaleRegion BRUNEI_DARUSSALAM = new LocaleRegion("Brunei Darussalam", "BN", "BRN", 96);

    public static final LocaleRegion BULGARIA = new LocaleRegion("Bulgaria", "BG", "BGR", 100);

    public static final LocaleRegion BURKINA_FASO = new LocaleRegion("Burkina Faso", "BF", "BFA", 854);

    public static final LocaleRegion BURUNDI = new LocaleRegion("Burundi", "BI", "BDI", 108);

    public static final LocaleRegion CAMBODIA = new LocaleRegion("Cambodia", "KH", "KHM", 116);

    public static final LocaleRegion CAMEROON = new LocaleRegion("Cameroon", "CM", "CMR", 120);

    public static final LocaleRegion CANADA = new LocaleRegion("Canada", "CA", "CAN", 124);

    public static final LocaleRegion CAPE_VERDE = new LocaleRegion("Cape Verde", "CV", "CPV", 132);

    public static final LocaleRegion CAYMAN_ISLANDS = new LocaleRegion("Cayman Islands", "KY", "CYM", 136);

    public static final LocaleRegion CENTRAL_AFRICAN_REPUBLIC = new LocaleRegion("Central African Republic", "CF", "CAF", 140);

    public static final LocaleRegion CHAD = new LocaleRegion("Chad", "TD", "TCD", 148);

    public static final LocaleRegion CHILE = new LocaleRegion("Chile", "CL", "CHL", 152);

    public static final LocaleRegion CHINA = new LocaleRegion("China", "CN", "CHN", 156);

    public static final LocaleRegion CHRISTMAS_ISLAND = new LocaleRegion("Christmas Island", "CX", "CXR", 162);

    public static final LocaleRegion COCOS_KEELING_ISLANDS = new LocaleRegion("Cocos Keeling Islands", "CC", "CCK", 166);

    public static final LocaleRegion COLOMBIA = new LocaleRegion("Colombia", "CO", "COL", 170);

    public static final LocaleRegion COMOROS = new LocaleRegion("Comoros", "KM", "COM", 174);

    public static final LocaleRegion CONGO_BRAZZAVILLE = new LocaleRegion("Congo Brazzaville", "CG", "COG", 178);

    public static final LocaleRegion CONGO_KINSHASA = new LocaleRegion("Congo Kinshasa", "CD", "COD", 180);

    public static final LocaleRegion COOK_ISLANDS = new LocaleRegion("Cook Islands", "CK", "COK", 184);

    @SuppressWarnings("SpellCheckingInspection")
    public static final LocaleRegion COSTA_RICA = new LocaleRegion("Costa Rica", "CR", "CRI", 188);

    @SuppressWarnings("SpellCheckingInspection")
    public static final LocaleRegion COTE_D_IVOIRE = new LocaleRegion("Cote D'Ivoire", "CI", "CIV", 384);

    public static final LocaleRegion CROATIA = new LocaleRegion("Croatia", "HR", "HRV", 191);

    public static final LocaleRegion CUBA = new LocaleRegion("Cuba", "CU", "CUB", 192);

    public static final LocaleRegion CYPRUS = new LocaleRegion("Cyprus", "CY", "CYP", 196);

    public static final LocaleRegion CZECH_REPUBLIC = new LocaleRegion("Czech Republic", "CZ", "CZE", 203);

    public static final LocaleRegion DENMARK = new LocaleRegion("Denmark", "DK", "DNK", 208);

    public static final LocaleRegion DJIBOUTI = new LocaleRegion("Djibouti", "DJ", "DJI", 262);

    public static final LocaleRegion DOMINICA = new LocaleRegion("Dominica", "DM", "DMA", 212);

    public static final LocaleRegion DOMINICAN_REPUBLIC = new LocaleRegion("Dominican Republic", "DO", "DOM", 214);

    public static final LocaleRegion ECUADOR = new LocaleRegion("Ecuador", "EC", "ECU", 218);

    public static final LocaleRegion EGYPT = new LocaleRegion("Egypt", "EG", "EGY", 818);

    public static final LocaleRegion EL_SALVADOR = new LocaleRegion("El Salvador", "SV", "SLV", 222);

    public static final LocaleRegion EQUATORIAL_GUINEA = new LocaleRegion("Equatorial Guinea", "GQ", "GNQ", 226);

    public static final LocaleRegion ERITREA = new LocaleRegion("Eritrea", "ER", "ERI", 232);

    public static final LocaleRegion ESTONIA = new LocaleRegion("Estonia", "EE", "EST", 233);

    public static final LocaleRegion ETHIOPIA = new LocaleRegion("Ethiopia", "ET", "ETH", 231);

    public static final LocaleRegion FALKLAND_ISLANDS = new LocaleRegion("Falkland Islands", "FK", "FLK", 260);

    @SuppressWarnings("SpellCheckingInspection")
    public static final LocaleRegion FAROE_ISLANDS = new LocaleRegion("Faroe Islands", "FO", "FRO", 234);

    public static final LocaleRegion FIJI = new LocaleRegion("Fiji", "FJ", "FJI", 242);

    public static final LocaleRegion FINLAND = new LocaleRegion("Finland", "FI", "FIN", 246);

    public static final LocaleRegion FRANCE = new LocaleRegion("France", "FR", "FRA", 250);

    public static final LocaleRegion FRENCH_GUIANA = new LocaleRegion("French Guiana", "GF", "GUF", 254);

    public static final LocaleRegion FRENCH_POLYNESIA = new LocaleRegion("French Polynesia", "PF", "PYF", 258);

    public static final LocaleRegion FRENCH_SOUTHERN_AND_ANTARCTIC_LANDS = new LocaleRegion("French Southern and Antarctic Lands", "TF", "ATF", 238);

    public static final LocaleRegion GABON = new LocaleRegion("Gabon", "GA", "GAB", 266);

    public static final LocaleRegion GAMBIA = new LocaleRegion("Gambia", "GM", "GMB", 270);

    public static final LocaleRegion GEORGIA = new LocaleRegion("Georgia", "GE", "GEO", 268);

    public static final LocaleRegion GERMANY = new LocaleRegion("Germany", "DE", "DEU", 276);

    public static final LocaleRegion GHANA = new LocaleRegion("Ghana", "GH", "GHA", 288);

    public static final LocaleRegion GIBRALTAR = new LocaleRegion("Gibraltar", "GI", "GIB", 292);

    public static final LocaleRegion GREECE = new LocaleRegion("Greece", "GR", "GRC", 300);

    public static final LocaleRegion GREENLAND = new LocaleRegion("Greenland", "GL", "GRL", 304);

    public static final LocaleRegion GRENADA = new LocaleRegion("Grenada", "GD", "GRD", 308);

    public static final LocaleRegion GUADELOUPE = new LocaleRegion("Guadeloupe", "GP", "GLP", 312);

    public static final LocaleRegion GUAM = new LocaleRegion("Guam", "GU", "GUM", 316);

    public static final LocaleRegion GUATEMALA = new LocaleRegion("Guatemala", "GT", "GTM", 320);

    public static final LocaleRegion GUERNSEY = new LocaleRegion("Guernsey", "GG", "GGY", 831);

    public static final LocaleRegion GUINEA = new LocaleRegion("Guinea", "GN", "GIN", 324);

    public static final LocaleRegion GUINEA_BISSAU = new LocaleRegion("Guinea Bissau", "GW", "GNB", 624);

    public static final LocaleRegion GUYANA = new LocaleRegion("Guyana", "GY", "GUY", 328);

    public static final LocaleRegion HAITI = new LocaleRegion("Haiti", "HT", "HTI", 332);

    public static final LocaleRegion HONDURAS = new LocaleRegion("Honduras", "HN", "HND", 340);

    public static final LocaleRegion HONG_KONG = new LocaleRegion("Hong Kong", "HK", "HKG", 344);

    public static final LocaleRegion HUNGARY = new LocaleRegion("Hungary", "HU", "HUN", 348);

    public static final LocaleRegion ICELAND = new LocaleRegion("Iceland", "IS", "ISL", 352);

    public static final LocaleRegion INDIA = new LocaleRegion("India", "IN", "IND", 356);

    public static final LocaleRegion INDONESIA = new LocaleRegion("Indonesia", "ID", "IDN", 360);

    public static final LocaleRegion IRAN = new LocaleRegion("Iran", "IR", "IRN", 364);

    public static final LocaleRegion IRAQ = new LocaleRegion("Iraq", "IQ", "IRQ", 368);

    public static final LocaleRegion IRELAND = new LocaleRegion("Ireland", "IE", "IRL", 372);

    public static final LocaleRegion ISLE_OF_MAN = new LocaleRegion("Isle of Man", "IM", "IMN", 833);

    public static final LocaleRegion ISRAEL = new LocaleRegion("Israel", "IL", "ISR", 376);

    public static final LocaleRegion ITALY = new LocaleRegion("Italy", "IT", "ITA", 380);

    public static final LocaleRegion JAMAICA = new LocaleRegion("Jamaica", "JM", "JAM", 388);

    public static final LocaleRegion JAPAN = new LocaleRegion("Japan", "JP", "JPN", 392);

    public static final LocaleRegion JERSEY = new LocaleRegion("Jersey", "JE", "JEY", 832);

    public static final LocaleRegion JORDAN = new LocaleRegion("Jordan", "JO", "JOR", 400);

    public static final LocaleRegion KAZAKHSTAN = new LocaleRegion("Kazakhstan", "KZ", "KAZ", 398);

    public static final LocaleRegion KENYA = new LocaleRegion("Kenya", "KE", "KEN", 404);

    public static final LocaleRegion KIRIBATI = new LocaleRegion("Kiribati", "KI", "KIR", 296);

    public static final LocaleRegion KOREA_NORTH = new LocaleRegion("Korea North", "KP", "PRK", 408);

    public static final LocaleRegion KOREA_SOUTH = new LocaleRegion("Korea South", "KR", "KOR", 410);

    public static final LocaleRegion KUWAIT = new LocaleRegion("Kuwait", "KW", "KWT", 414);

    public static final LocaleRegion KYRGYZSTAN = new LocaleRegion("Kyrgyzstan", "KG", "KGZ", 417);

    public static final LocaleRegion LAOS = new LocaleRegion("Laos", "LA", "LAO", 418);

    public static final LocaleRegion LATVIA = new LocaleRegion("Latvia", "LV", "LVA", 428);

    public static final LocaleRegion LEBANON = new LocaleRegion("Lebanon", "LB", "LBN", 422);

    public static final LocaleRegion LESOTHO = new LocaleRegion("Lesotho", "LS", "LSO", 426);

    public static final LocaleRegion LIBERIA = new LocaleRegion("Liberia", "LR", "LBR", 430);

    public static final LocaleRegion LIBYA = new LocaleRegion("Libya", "LY", "LBY", 434);

    public static final LocaleRegion LIECHTENSTEIN = new LocaleRegion("Liechtenstein", "LI", "LIE", 438);

    public static final LocaleRegion LITHUANIA = new LocaleRegion("Lithuania", "LT", "LTU", 440);

    public static final LocaleRegion LUXEMBOURG = new LocaleRegion("Luxembourg", "LU", "LUX", 442);

    public static final LocaleRegion MACAU = new LocaleRegion("Macau", "MO", "MAC", 446);

    public static final LocaleRegion MACEDONIA = new LocaleRegion("Macedonia", "MK", "MKD", 807);

    public static final LocaleRegion MADAGASCAR = new LocaleRegion("Madagascar", "MG", "MDG", 450);

    public static final LocaleRegion MALAWI = new LocaleRegion("Malawi", "MW", "MWI", 454);

    public static final LocaleRegion MALAYSIA = new LocaleRegion("Malaysia", "MY", "MYS", 458);

    public static final LocaleRegion MALDIVES = new LocaleRegion("Maldives", "MV", "MDV", 462);

    public static final LocaleRegion MALI = new LocaleRegion("Mali", "ML", "MLI", 466);

    public static final LocaleRegion MALTA = new LocaleRegion("Malta", "MT", "MLT", 470);

    public static final LocaleRegion MARSHALL_ISLANDS = new LocaleRegion("Marshall Islands", "MH", "MHL", 584);

    public static final LocaleRegion MARTINIQUE = new LocaleRegion("Martinique", "MQ", "MTQ", 474);

    public static final LocaleRegion MAURITANIA = new LocaleRegion("Mauritania", "MR", "MRT", 478);

    public static final LocaleRegion MAURITIUS = new LocaleRegion("Mauritius", "MU", "MUS", 480);

    @SuppressWarnings("SpellCheckingInspection")
    public static final LocaleRegion MAYOTTE = new LocaleRegion("Mayotte", "YT", "MYT", 175);

    public static final LocaleRegion MEXICO = new LocaleRegion("Mexico", "MX", "MEX", 484);

    public static final LocaleRegion MICRONESIA = new LocaleRegion("Micronesia", "FM", "FSM", 583);

    public static final LocaleRegion MOLDOVA = new LocaleRegion("Moldova", "MD", "MDA", 498);

    public static final LocaleRegion MONACO = new LocaleRegion("Monaco", "MC", "MCO", 492);

    public static final LocaleRegion MONGOLIA = new LocaleRegion("Mongolia", "MN", "MNG", 496);

    public static final LocaleRegion MONTENEGRO = new LocaleRegion("Montenegro", "ME", "MNE", 499);

    public static final LocaleRegion MONTSERRAT = new LocaleRegion("Montserrat", "MS", "MSR", 500);

    public static final LocaleRegion MOROCCO = new LocaleRegion("Morocco", "MA", "MAR", 504);

    public static final LocaleRegion MOZAMBIQUE = new LocaleRegion("Mozambique", "MZ", "MOZ", 508);

    public static final LocaleRegion MYANMAR = new LocaleRegion("Myanmar", "MM", "MMR", 104);

    public static final LocaleRegion NAMIBIA = new LocaleRegion("Namibia", "NA", "NAM", 516);

    public static final LocaleRegion NAURU = new LocaleRegion("Nauru", "NR", "NRU", 520);

    public static final LocaleRegion NEPAL = new LocaleRegion("Nepal", "NP", "NPL", 524);

    public static final LocaleRegion NETHERLANDS = new LocaleRegion("Netherlands", "NL", "NLD", 528);

    public static final LocaleRegion NETHERLANDS_ANTILLES = new LocaleRegion("Netherlands Antilles", "AN", "ANT", 530);

    public static final LocaleRegion NEW_CALEDONIA = new LocaleRegion("New Caledonia", "NC", "NCL", 540);

    public static final LocaleRegion NEW_ZEALAND = new LocaleRegion("New Zealand", "NZ", "NZL", 554);

    public static final LocaleRegion NICARAGUA = new LocaleRegion("Nicaragua", "NI", "NIC", 558);

    public static final LocaleRegion NIGER = new LocaleRegion("Niger", "NE", "NER", 562);

    public static final LocaleRegion NIGERIA = new LocaleRegion("Nigeria", "NG", "NGA", 566);

    @SuppressWarnings("SpellCheckingInspection")
    public static final LocaleRegion NIUE = new LocaleRegion("Niue", "NU", "NIU", 570);

    public static final LocaleRegion NORFOLK_ISLAND = new LocaleRegion("Norfolk Island", "NF", "NFK", 574);

    public static final LocaleRegion NORTHERN_MARIANA_ISLANDS = new LocaleRegion("Northern Mariana Islands", "MP", "MNP", 580);

    public static final LocaleRegion NORWAY = new LocaleRegion("Norway", "NO", "NOR", 578);

    public static final LocaleRegion OMAN = new LocaleRegion("Oman", "OM", "OMN", 512);

    public static final LocaleRegion PAKISTAN = new LocaleRegion("Pakistan", "PK", "PAK", 586);

    @SuppressWarnings("SpellCheckingInspection")
    public static final LocaleRegion PALAU = new LocaleRegion("Palau", "PW", "PLW", 585);

    public static final LocaleRegion PALESTINE = new LocaleRegion("Palestine", "PS", "PSE", 275);

    public static final LocaleRegion PANAMA = new LocaleRegion("Panama", "PA", "PAN", 591);

    public static final LocaleRegion PAPUA_NEW_GUINEA = new LocaleRegion("Papua New Guinea", "PG", "PNG", 598);

    public static final LocaleRegion PARAGUAY = new LocaleRegion("Paraguay", "PY", "PRY", 600);

    public static final LocaleRegion PERU = new LocaleRegion("Peru", "PE", "PER", 604);

    public static final LocaleRegion PHILIPPINES = new LocaleRegion("Philippines", "PH", "PHL", 608);

    public static final LocaleRegion PITCAIRN = new LocaleRegion("Pitcairn", "PN", "PCN", 612);

    public static final LocaleRegion POLAND = new LocaleRegion("Poland", "PL", "POL", 616);

    public static final LocaleRegion PORTUGAL = new LocaleRegion("Portugal", "PT", "PRT", 620);

    public static final LocaleRegion PUERTO_RICO = new LocaleRegion("Puerto Rico", "PR", "PRI", 630);

    public static final LocaleRegion QATAR = new LocaleRegion("Qatar", "QA", "QAT", 634);

    public static final LocaleRegion REUNION = new LocaleRegion("Reunion", "RE", "REU", 638);

    public static final LocaleRegion ROMANIA = new LocaleRegion("Romania", "RO", "ROU", 642);

    public static final LocaleRegion RUSSIA = new LocaleRegion("Russia", "RU", "RUS", 643);

    public static final LocaleRegion RWANDA = new LocaleRegion("Rwanda", "RW", "RWA", 646);

    @SuppressWarnings("SpellCheckingInspection")
    public static final LocaleRegion SAINT_BARTHELEMY = new LocaleRegion("Saint Barthelemy", "BL", "BLM", 652);

    public static final LocaleRegion SAINT_HELENA = new LocaleRegion("Saint Helena", "SH", "SHN", 654);

    @SuppressWarnings("SpellCheckingInspection")
    public static final LocaleRegion SAINT_KITTS_AND_NEVIS = new LocaleRegion("Saint Kitts and Nevis", "KN", "KNA", 659);

    public static final LocaleRegion SAINT_LUCIA = new LocaleRegion("Saint Lucia", "LC", "LCA", 662);

    public static final LocaleRegion SAINT_MARTIN_FRENCH_PART = new LocaleRegion("Saint Martin - French Part", "MF", "MAF", 663);

    public static final LocaleRegion SAINT_PIERRE_AND_MIQUELON = new LocaleRegion("Saint Pierre and Miquelon", "PM", "SPM", 666);

    public static final LocaleRegion SAINT_VINCENT_AND_THE_GRENADINES = new LocaleRegion("Saint Vincent and the Grenadines", "VC", "VCT", 670);

    public static final LocaleRegion SAMOA = new LocaleRegion("Samoa", "WS", "WSM", 882);

    public static final LocaleRegion SAN_MARINO = new LocaleRegion("San Marino", "SM", "SMR", 674);

    public static final LocaleRegion SAO_TOME_AND_PRINCIPE = new LocaleRegion("Sao Tome And Principe", "ST", "STP", 678);

    public static final LocaleRegion SAUDI_ARABIA = new LocaleRegion("Saudi Arabia", "SA", "SAU", 682);

    public static final LocaleRegion SENEGAL = new LocaleRegion("Senegal", "SN", "SEN", 686);

    public static final LocaleRegion SERBIA = new LocaleRegion("Serbia", "RS", "SRB", 688);

    public static final LocaleRegion SEYCHELLES = new LocaleRegion("Seychelles", "SC", "SYC", 690);

    public static final LocaleRegion SIERRA_LEONE = new LocaleRegion("Sierra Leone", "SL", "SLE", 694);

    public static final LocaleRegion SINGAPORE = new LocaleRegion("Singapore", "SG", "SGP", 702);

    public static final LocaleRegion SLOVAKIA = new LocaleRegion("Slovakia", "SK", "SVK", 703);

    public static final LocaleRegion SLOVENIA = new LocaleRegion("Slovenia", "SI", "SVN", 705);

    public static final LocaleRegion SOLOMON_ISLANDS = new LocaleRegion("Solomon Islands", "SB", "SLB", 90);

    public static final LocaleRegion SOMALIA = new LocaleRegion("Somalia", "SO", "SOM", 706);

    public static final LocaleRegion SOUTH_AFRICA = new LocaleRegion("South Africa", "ZA", "ZAF", 710);

    public static final LocaleRegion SOUTH_GEORGIA_AND_SOUTH_SANDWICH_ISLANDS = new LocaleRegion("South Georgia and South Sandwich Islands", "GS", "SGS", 239);

    public static final LocaleRegion SOUTH_SUDAN = new LocaleRegion("South Sudan", "SS", "SSD", 728);

    public static final LocaleRegion SPAIN = new LocaleRegion("Spain", "ES", "ESP", 724);

    public static final LocaleRegion SRI_LANKA = new LocaleRegion("Sri Lanka", "LK", "LKA", 144);

    public static final LocaleRegion SUDAN = new LocaleRegion("Sudan", "SD", "SDN", 729);

    public static final LocaleRegion SURINAME = new LocaleRegion("Suriname", "SR", "SUR", 740);

    @SuppressWarnings("SpellCheckingInspection")
    public static final LocaleRegion SVALBARD_AND_JAN_MAYEN_ISLANDS = new LocaleRegion("Svalbard and Jan Mayen Islands", "SJ", "SJM", 744);

    public static final LocaleRegion SWAZILAND = new LocaleRegion("Swaziland", "SZ", "SWZ", 748);

    public static final LocaleRegion SWEDEN = new LocaleRegion("Sweden", "SE", "SWE", 752);

    public static final LocaleRegion SWITZERLAND = new LocaleRegion("Switzerland", "CH", "CHE", 756);

    public static final LocaleRegion SYRIA = new LocaleRegion("Syria", "SY", "SYR", 760);

    public static final LocaleRegion TAIWAN = new LocaleRegion("Taiwan", "TW", "TWN", 158);

    public static final LocaleRegion TAJIKISTAN = new LocaleRegion("Tajikistan", "TJ", "TJK", 762);

    public static final LocaleRegion TANZANIA = new LocaleRegion("Tanzania", "TZ", "TZA", 834);

    public static final LocaleRegion THAILAND = new LocaleRegion("Thailand", "TH", "THA", 764);

    public static final LocaleRegion TIMOR_LESTE = new LocaleRegion("Timor Leste", "TL", "TLS", 626);

    public static final LocaleRegion TOGO = new LocaleRegion("Togo", "TG", "TGO", 768);

    @SuppressWarnings("SpellCheckingInspection")
    public static final LocaleRegion TOKELAU = new LocaleRegion("Tokelau", "TK", "TKL", 772);

    public static final LocaleRegion TONGA = new LocaleRegion("Tonga", "TO", "TON", 776);

    public static final LocaleRegion TRINIDAD_AND_TOBAGO = new LocaleRegion("Trinidad And Tobago", "TT", "TTO", 780);

    public static final LocaleRegion TUNISIA = new LocaleRegion("Tunisia", "TN", "TUN", 788);

    public static final LocaleRegion TURKEY = new LocaleRegion("Turkey", "TR", "TUR", 792);

    public static final LocaleRegion TURKMENISTAN = new LocaleRegion("Turkmenistan", "TM", "TKM", 795);

    @SuppressWarnings("SpellCheckingInspection")
    public static final LocaleRegion TURKS_AND_CAICOS_ISLANDS = new LocaleRegion("Turks and Caicos Islands", "TC", "TCA", 796);

    public static final LocaleRegion TUVALU = new LocaleRegion("Tuvalu", "TV", "TUV", 798);

    public static final LocaleRegion UGANDA = new LocaleRegion("UGANDA", "UG", "UGA", 800);

    public static final LocaleRegion UKRAINE = new LocaleRegion("Ukraine", "UA", "UKR", 804);

    public static final LocaleRegion UNITED_ARAB_EMIRATES = new LocaleRegion("United Arab Emirates", "AE", "ARE", 784);

    public static final LocaleRegion UNITED_KINGDOM = new LocaleRegion("United Kingdom", "GB", "GBR", 826);

    public static final LocaleRegion UNITED_STATES = new LocaleRegion("United States", "US", "USA", 840);

    public static final LocaleRegion UNITED_STATES_MINOR_OUTLYING_ISLANDS = new LocaleRegion("United States Minor Outlying Islands", "UM", "UMI", 581);

    public static final LocaleRegion URUGUAY = new LocaleRegion("Uruguay", "UY", "URY", 858);

    public static final LocaleRegion UZBEKISTAN = new LocaleRegion("Uzbekistan", "UZ", "UZB", 860);

    public static final LocaleRegion VANUATU = new LocaleRegion("Vanuatu", "VU", "VUT", 548);

    public static final LocaleRegion VATICAN = new LocaleRegion("Vatican", "VA", "VAT", 336);

    public static final LocaleRegion VENEZUELA = new LocaleRegion("Venezuela", "VE", "VEN", 862);

    public static final LocaleRegion VIETNAM = new LocaleRegion("Vietnam", "VN", "VNM", 704);

    public static final LocaleRegion VIRGIN_ISLANDS = new LocaleRegion("Virgin Islands", "VI", "VIR", 850);

    public static final LocaleRegion VIRGIN_ISLANDS_BRITISH = new LocaleRegion("Virgin Islands British", "VG", "VGB", 92);

    @SuppressWarnings("SpellCheckingInspection")
    public static final LocaleRegion WALLIS_AND_FUTUNA_ISLANDS = new LocaleRegion("Wallis And Futuna Islands", "WF", "WLF", 876);

    public static final LocaleRegion WESTERN_SAHARA = new LocaleRegion("Western Sahara", "EH", "ESH", 732);

    public static final LocaleRegion YEMEN = new LocaleRegion("Yemen", "YE", "YEM", 887);

    public static final LocaleRegion ZAMBIA = new LocaleRegion("Zambia", "ZM", "ZMB", 894);

    public static final LocaleRegion ZIMBABWE = new LocaleRegion("Zimbabwe", "ZW", "ZWE", 716);

    /** The name of this country */
    private final String name;

    /** ISO alpha2 country code (2 letters) */
    private final String alpha2CountryCode;

    /** ISO alpha3 country code (3 letters) */
    private final String alpha3CountryCode;

    /** ISO numeric country code (3 digit) */
    private final int numericCountryCode;

    public LocaleRegion(String name,
                        String alpha2CountryCode,
                        String alpha3CountryCode,
                        int numericCountryCode)
    {
        this.name = name;
        this.alpha2CountryCode = alpha2CountryCode;
        this.alpha3CountryCode = alpha3CountryCode;
        this.numericCountryCode = numericCountryCode;
    }

    /**
     * Get the alpha2 code (2 letters)
     *
     * @return 2 letters' alpha2 code
     */
    public String alpha2Code()
    {
        return alpha2CountryCode;
    }

    /**
     * Get the alpha3 code (3 letters)
     *
     * @return 3 letters' alpha3 code
     */
    @KivaKitExcludeProperty
    public String alpha3Code()
    {
        return alpha3CountryCode;
    }

    /**
     * This class defines the ISO country name, which can be represented by: a two-letter code (alpha-2) which is
     * recommended as the general purpose code and a three-letter code (alpha-3) which is more closely related to the
     * country name. More detail please refer to https://www.iso.org/iso/country_codes.htm.
     */
    @SuppressWarnings("JavadocLinkAsPlainText")
    @Override
    public boolean equals(Object object)
    {
        if (object instanceof LocaleRegion)
        {
            var that = (LocaleRegion) object;
            return alpha2CountryCode.equals(that.alpha2CountryCode);
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode()
    {
        return alpha2CountryCode.hashCode();
    }

    /**
     * Returns the name of the country
     */
    public String name()
    {
        return name;
    }

    /**
     * Get the numeric code (3 digits)
     *
     * @return 3 digits numeric code
     */
    public int numericCountryCode()
    {
        return numericCountryCode;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        return alpha2CountryCode;
    }
}
