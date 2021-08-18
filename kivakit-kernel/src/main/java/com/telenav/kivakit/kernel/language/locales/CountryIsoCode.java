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

package com.telenav.kivakit.kernel.language.locales;

import com.telenav.kivakit.kernel.language.reflection.property.KivaKitExcludeProperty;

/**
 * This class defines countries by name and relevant ISO codes. Provides the ISO country code, which can be represented
 * by: a two-letter code (alpha-2) is recommended as the general purpose code. Also provided is a three-letter code
 * (alpha-3) which is more closely related to the country name, and an ISO number. More detail please refer to
 * https://www.iso.org/iso/country_codes.htm.
 *
 * @author Junwei
 * @author jonathanl (shibo)
 * @version 1.0.0 2012-7-12
 */
public class CountryIsoCode
{
    public static final CountryIsoCode AFGHANISTAN = new CountryIsoCode("Afghanistan", "AF", "AFG", 4);

    public static final CountryIsoCode ALAND = new CountryIsoCode("Aland", "AX", "ALA", 248);

    public static final CountryIsoCode ALBANIA = new CountryIsoCode("Albania", "AL", "ALB", 8);

    public static final CountryIsoCode ALGERIA = new CountryIsoCode("Algeria", "DZ", "DZA", 12);

    public static final CountryIsoCode AMERICAN_SAMOA = new CountryIsoCode("American Samoa", "AS", "ASM", 16);

    public static final CountryIsoCode ANDORRA = new CountryIsoCode("Andorra", "AD", "AND", 20);

    public static final CountryIsoCode ANGOLA = new CountryIsoCode("Angola", "AO", "AGO", 24);

    public static final CountryIsoCode ANGUILLA = new CountryIsoCode("Anguilla", "AI", "AIA", 660);

    public static final CountryIsoCode ANTIGUA_AND_BARBUDA = new CountryIsoCode("Antigua And Barbuda", "AG", "ATG", 28);

    public static final CountryIsoCode ARGENTINA = new CountryIsoCode("Argentina", "AR", "ARG", 32);

    public static final CountryIsoCode ARMENIA = new CountryIsoCode("Armenia", "AM", "ARM", 51);

    public static final CountryIsoCode ARUBA = new CountryIsoCode("Aruba", "AW", "ABW", 533);

    public static final CountryIsoCode AUSTRALIA = new CountryIsoCode("Australia", "AU", "AUS", 36);

    public static final CountryIsoCode AUSTRIA = new CountryIsoCode("Austria", "AT", "AUT", 40);

    public static final CountryIsoCode AZERBAIJAN = new CountryIsoCode("Azerbaijan", "AZ", "AZE", 31);

    public static final CountryIsoCode BAHAMAS = new CountryIsoCode("Bahamas", "BS", "BHS", 44);

    public static final CountryIsoCode BAHRAIN = new CountryIsoCode("Bahrain", "BH", "BHR", 48);

    public static final CountryIsoCode BANGLADESH = new CountryIsoCode("Bangladesh", "BD", "BGD", 50);

    public static final CountryIsoCode BARBADOS = new CountryIsoCode("Barbados", "BB", "BRB", 52);

    public static final CountryIsoCode BELARUS = new CountryIsoCode("Belarus", "BY", "BLR", 112);

    public static final CountryIsoCode BELGIUM = new CountryIsoCode("Belgium", "BE", "BEL", 56);

    public static final CountryIsoCode BELIZE = new CountryIsoCode("Belize", "BZ", "BLZ", 84);

    public static final CountryIsoCode BENIN = new CountryIsoCode("Benin", "BJ", "BEN", 204);

    public static final CountryIsoCode BERMUDA = new CountryIsoCode("Bermuda", "BM", "BMU", 60);

    public static final CountryIsoCode BHUTAN = new CountryIsoCode("Bhutan", "BT", "BTN", 64);

    public static final CountryIsoCode BOLIVIA = new CountryIsoCode("Bolivia", "BO", "BOL", 68);

    public static final CountryIsoCode BOSNIA_AND_HERZEGOVINA = new CountryIsoCode("Bosnia And Herzegovina", "BA", "BIH", 70);

    public static final CountryIsoCode BOTSWANA = new CountryIsoCode("Botswana", "BW", "BWA", 72);

    public static final CountryIsoCode BRAZIL = new CountryIsoCode("Brazil", "BR", "BRA", 76);

    public static final CountryIsoCode BRITISH_INDIAN_OCEAN_TERRITORY = new CountryIsoCode("British Indian Ocean Territory", "IO", "IOT", 86);

    public static final CountryIsoCode BRUNEI_DARUSSALAM = new CountryIsoCode("Brunei Darussalam", "BN", "BRN", 96);

    public static final CountryIsoCode BULGARIA = new CountryIsoCode("Bulgaria", "BG", "BGR", 100);

    public static final CountryIsoCode BURKINA_FASO = new CountryIsoCode("Burkina Faso", "BF", "BFA", 854);

    public static final CountryIsoCode BURUNDI = new CountryIsoCode("Burundi", "BI", "BDI", 108);

    public static final CountryIsoCode CAMBODIA = new CountryIsoCode("Cambodia", "KH", "KHM", 116);

    public static final CountryIsoCode CAMEROON = new CountryIsoCode("Cameroon", "CM", "CMR", 120);

    public static final CountryIsoCode CANADA = new CountryIsoCode("Canada", "CA", "CAN", 124);

    public static final CountryIsoCode CAPE_VERDE = new CountryIsoCode("Cape Verde", "CV", "CPV", 132);

    public static final CountryIsoCode CAYMAN_ISLANDS = new CountryIsoCode("Cayman Islands", "KY", "CYM", 136);

    public static final CountryIsoCode CENTRAL_AFRICAN_REPUBLIC = new CountryIsoCode("Central African Republic", "CF", "CAF", 140);

    public static final CountryIsoCode CHAD = new CountryIsoCode("Chad", "TD", "TCD", 148);

    public static final CountryIsoCode CHILE = new CountryIsoCode("Chile", "CL", "CHL", 152);

    public static final CountryIsoCode CHINA = new CountryIsoCode("China", "CN", "CHN", 156);

    public static final CountryIsoCode CHRISTMAS_ISLAND = new CountryIsoCode("Christmas Island", "CX", "CXR", 162);

    public static final CountryIsoCode COCOS_KEELING_ISLANDS = new CountryIsoCode("Cocos Keeling Islands", "CC", "CCK", 166);

    public static final CountryIsoCode COLOMBIA = new CountryIsoCode("Colombia", "CO", "COL", 170);

    public static final CountryIsoCode COMOROS = new CountryIsoCode("Comoros", "KM", "COM", 174);

    public static final CountryIsoCode CONGO_BRAZZAVILLE = new CountryIsoCode("Congo Brazzaville", "CG", "COG", 178);

    public static final CountryIsoCode CONGO_KINSHASA = new CountryIsoCode("Congo Kinshasa", "CD", "COD", 180);

    public static final CountryIsoCode COOK_ISLANDS = new CountryIsoCode("Cook Islands", "CK", "COK", 184);

    public static final CountryIsoCode COSTA_RICA = new CountryIsoCode("Costa Rica", "CR", "CRI", 188);

    public static final CountryIsoCode COTE_D_IVOIRE = new CountryIsoCode("Cote D'Ivoire", "CI", "CIV", 384);

    public static final CountryIsoCode CROATIA = new CountryIsoCode("Croatia", "HR", "HRV", 191);

    public static final CountryIsoCode CUBA = new CountryIsoCode("Cuba", "CU", "CUB", 192);

    public static final CountryIsoCode CYPRUS = new CountryIsoCode("Cyprus", "CY", "CYP", 196);

    public static final CountryIsoCode CZECH_REPUBLIC = new CountryIsoCode("Czech Republic", "CZ", "CZE", 203);

    public static final CountryIsoCode DENMARK = new CountryIsoCode("Denmark", "DK", "DNK", 208);

    public static final CountryIsoCode DJIBOUTI = new CountryIsoCode("Djibouti", "DJ", "DJI", 262);

    public static final CountryIsoCode DOMINICA = new CountryIsoCode("Dominica", "DM", "DMA", 212);

    public static final CountryIsoCode DOMINICAN_REPUBLIC = new CountryIsoCode("Dominican Republic", "DO", "DOM", 214);

    public static final CountryIsoCode ECUADOR = new CountryIsoCode("Ecuador", "EC", "ECU", 218);

    public static final CountryIsoCode EGYPT = new CountryIsoCode("Egypt", "EG", "EGY", 818);

    public static final CountryIsoCode EL_SALVADOR = new CountryIsoCode("El Salvador", "SV", "SLV", 222);

    public static final CountryIsoCode EQUATORIAL_GUINEA = new CountryIsoCode("Equatorial Guinea", "GQ", "GNQ", 226);

    public static final CountryIsoCode ERITREA = new CountryIsoCode("Eritrea", "ER", "ERI", 232);

    public static final CountryIsoCode ESTONIA = new CountryIsoCode("Estonia", "EE", "EST", 233);

    public static final CountryIsoCode ETHIOPIA = new CountryIsoCode("Ethiopia", "ET", "ETH", 231);

    public static final CountryIsoCode FALKLAND_ISLANDS = new CountryIsoCode("Falkland Islands", "FK", "FLK", 260);

    public static final CountryIsoCode FAROE_ISLANDS = new CountryIsoCode("Faroe Islands", "FO", "FRO", 234);

    public static final CountryIsoCode FIJI = new CountryIsoCode("Fiji", "FJ", "FJI", 242);

    public static final CountryIsoCode FINLAND = new CountryIsoCode("Finland", "FI", "FIN", 246);

    public static final CountryIsoCode FRANCE = new CountryIsoCode("France", "FR", "FRA", 250);

    public static final CountryIsoCode FRENCH_GUIANA = new CountryIsoCode("French Guiana", "GF", "GUF", 254);

    public static final CountryIsoCode FRENCH_POLYNESIA = new CountryIsoCode("French Polynesia", "PF", "PYF", 258);

    public static final CountryIsoCode FRENCH_SOUTHERN_AND_ANTARCTIC_LANDS = new CountryIsoCode("French Southern and Antarctic Lands", "TF", "ATF", 238);

    public static final CountryIsoCode GABON = new CountryIsoCode("Gabon", "GA", "GAB", 266);

    public static final CountryIsoCode GAMBIA = new CountryIsoCode("Gambia", "GM", "GMB", 270);

    public static final CountryIsoCode GEORGIA = new CountryIsoCode("Georgia", "GE", "GEO", 268);

    public static final CountryIsoCode GERMANY = new CountryIsoCode("Germany", "DE", "DEU", 276);

    public static final CountryIsoCode GHANA = new CountryIsoCode("Ghana", "GH", "GHA", 288);

    public static final CountryIsoCode GIBRALTAR = new CountryIsoCode("Gibraltar", "GI", "GIB", 292);

    public static final CountryIsoCode GREECE = new CountryIsoCode("Greece", "GR", "GRC", 300);

    public static final CountryIsoCode GREENLAND = new CountryIsoCode("Greenland", "GL", "GRL", 304);

    public static final CountryIsoCode GRENADA = new CountryIsoCode("Grenada", "GD", "GRD", 308);

    public static final CountryIsoCode GUADELOUPE = new CountryIsoCode("Guadeloupe", "GP", "GLP", 312);

    public static final CountryIsoCode GUAM = new CountryIsoCode("Guam", "GU", "GUM", 316);

    public static final CountryIsoCode GUATEMALA = new CountryIsoCode("Guatemala", "GT", "GTM", 320);

    public static final CountryIsoCode GUERNSEY = new CountryIsoCode("Guernsey", "GG", "GGY", 831);

    public static final CountryIsoCode GUINEA = new CountryIsoCode("Guinea", "GN", "GIN", 324);

    public static final CountryIsoCode GUINEA_BISSAU = new CountryIsoCode("Guinea Bissau", "GW", "GNB", 624);

    public static final CountryIsoCode GUYANA = new CountryIsoCode("Guyana", "GY", "GUY", 328);

    public static final CountryIsoCode HAITI = new CountryIsoCode("Haiti", "HT", "HTI", 332);

    public static final CountryIsoCode HONDURAS = new CountryIsoCode("Honduras", "HN", "HND", 340);

    public static final CountryIsoCode HONG_KONG = new CountryIsoCode("Hong Kong", "HK", "HKG", 344);

    public static final CountryIsoCode HUNGARY = new CountryIsoCode("Hungary", "HU", "HUN", 348);

    public static final CountryIsoCode ICELAND = new CountryIsoCode("Iceland", "IS", "ISL", 352);

    public static final CountryIsoCode INDIA = new CountryIsoCode("India", "IN", "IND", 356);

    public static final CountryIsoCode INDONESIA = new CountryIsoCode("Indonesia", "ID", "IDN", 360);

    public static final CountryIsoCode IRAN = new CountryIsoCode("Iran", "IR", "IRN", 364);

    public static final CountryIsoCode IRAQ = new CountryIsoCode("Iraq", "IQ", "IRQ", 368);

    public static final CountryIsoCode IRELAND = new CountryIsoCode("Ireland", "IE", "IRL", 372);

    public static final CountryIsoCode ISLE_OF_MAN = new CountryIsoCode("Isle of Man", "IM", "IMN", 833);

    public static final CountryIsoCode ISRAEL = new CountryIsoCode("Israel", "IL", "ISR", 376);

    public static final CountryIsoCode ITALY = new CountryIsoCode("Italy", "IT", "ITA", 380);

    public static final CountryIsoCode JAMAICA = new CountryIsoCode("Jamaica", "JM", "JAM", 388);

    public static final CountryIsoCode JAPAN = new CountryIsoCode("Japan", "JP", "JPN", 392);

    public static final CountryIsoCode JERSEY = new CountryIsoCode("Jersey", "JE", "JEY", 832);

    public static final CountryIsoCode JORDAN = new CountryIsoCode("Jordan", "JO", "JOR", 400);

    public static final CountryIsoCode KAZAKHSTAN = new CountryIsoCode("Kazakhstan", "KZ", "KAZ", 398);

    public static final CountryIsoCode KENYA = new CountryIsoCode("Kenya", "KE", "KEN", 404);

    public static final CountryIsoCode KIRIBATI = new CountryIsoCode("Kiribati", "KI", "KIR", 296);

    public static final CountryIsoCode KOREA_NORTH = new CountryIsoCode("Korea North", "KP", "PRK", 408);

    public static final CountryIsoCode KOREA_SOUTH = new CountryIsoCode("Korea South", "KR", "KOR", 410);

    public static final CountryIsoCode KUWAIT = new CountryIsoCode("Kuwait", "KW", "KWT", 414);

    public static final CountryIsoCode KYRGYZSTAN = new CountryIsoCode("Kyrgyzstan", "KG", "KGZ", 417);

    public static final CountryIsoCode LAOS = new CountryIsoCode("Laos", "LA", "LAO", 418);

    public static final CountryIsoCode LATVIA = new CountryIsoCode("Latvia", "LV", "LVA", 428);

    public static final CountryIsoCode LEBANON = new CountryIsoCode("Lebanon", "LB", "LBN", 422);

    public static final CountryIsoCode LESOTHO = new CountryIsoCode("Lesotho", "LS", "LSO", 426);

    public static final CountryIsoCode LIBERIA = new CountryIsoCode("Liberia", "LR", "LBR", 430);

    public static final CountryIsoCode LIBYA = new CountryIsoCode("Libya", "LY", "LBY", 434);

    public static final CountryIsoCode LIECHTENSTEIN = new CountryIsoCode("Liechtenstein", "LI", "LIE", 438);

    public static final CountryIsoCode LITHUANIA = new CountryIsoCode("Lithuania", "LT", "LTU", 440);

    public static final CountryIsoCode LUXEMBOURG = new CountryIsoCode("Luxembourg", "LU", "LUX", 442);

    public static final CountryIsoCode MACAU = new CountryIsoCode("Macau", "MO", "MAC", 446);

    public static final CountryIsoCode MACEDONIA = new CountryIsoCode("Macedonia", "MK", "MKD", 807);

    public static final CountryIsoCode MADAGASCAR = new CountryIsoCode("Madagascar", "MG", "MDG", 450);

    public static final CountryIsoCode MALAWI = new CountryIsoCode("Malawi", "MW", "MWI", 454);

    public static final CountryIsoCode MALAYSIA = new CountryIsoCode("Malaysia", "MY", "MYS", 458);

    public static final CountryIsoCode MALDIVES = new CountryIsoCode("Maldives", "MV", "MDV", 462);

    public static final CountryIsoCode MALI = new CountryIsoCode("Mali", "ML", "MLI", 466);

    public static final CountryIsoCode MALTA = new CountryIsoCode("Malta", "MT", "MLT", 470);

    public static final CountryIsoCode MARSHALL_ISLANDS = new CountryIsoCode("Marshall Islands", "MH", "MHL", 584);

    public static final CountryIsoCode MARTINIQUE = new CountryIsoCode("Martinique", "MQ", "MTQ", 474);

    public static final CountryIsoCode MAURITANIA = new CountryIsoCode("Mauritania", "MR", "MRT", 478);

    public static final CountryIsoCode MAURITIUS = new CountryIsoCode("Mauritius", "MU", "MUS", 480);

    public static final CountryIsoCode MAYOTTE = new CountryIsoCode("Mayotte", "YT", "MYT", 175);

    public static final CountryIsoCode MEXICO = new CountryIsoCode("Mexico", "MX", "MEX", 484);

    public static final CountryIsoCode MICRONESIA = new CountryIsoCode("Micronesia", "FM", "FSM", 583);

    public static final CountryIsoCode MOLDOVA = new CountryIsoCode("Moldova", "MD", "MDA", 498);

    public static final CountryIsoCode MONACO = new CountryIsoCode("Monaco", "MC", "MCO", 492);

    public static final CountryIsoCode MONGOLIA = new CountryIsoCode("Mongolia", "MN", "MNG", 496);

    public static final CountryIsoCode MONTENEGRO = new CountryIsoCode("Montenegro", "ME", "MNE", 499);

    public static final CountryIsoCode MONTSERRAT = new CountryIsoCode("Montserrat", "MS", "MSR", 500);

    public static final CountryIsoCode MOROCCO = new CountryIsoCode("Morocco", "MA", "MAR", 504);

    public static final CountryIsoCode MOZAMBIQUE = new CountryIsoCode("Mozambique", "MZ", "MOZ", 508);

    public static final CountryIsoCode MYANMAR = new CountryIsoCode("Myanmar", "MM", "MMR", 104);

    public static final CountryIsoCode NAMIBIA = new CountryIsoCode("Namibia", "NA", "NAM", 516);

    public static final CountryIsoCode NAURU = new CountryIsoCode("Nauru", "NR", "NRU", 520);

    public static final CountryIsoCode NEPAL = new CountryIsoCode("Nepal", "NP", "NPL", 524);

    public static final CountryIsoCode NETHERLANDS = new CountryIsoCode("Netherlands", "NL", "NLD", 528);

    public static final CountryIsoCode NETHERLANDS_ANTILLES = new CountryIsoCode("Netherlands Antilles", "AN", "ANT", 530);

    public static final CountryIsoCode NEW_CALEDONIA = new CountryIsoCode("New Caledonia", "NC", "NCL", 540);

    public static final CountryIsoCode NEW_ZEALAND = new CountryIsoCode("New Zealand", "NZ", "NZL", 554);

    public static final CountryIsoCode NICARAGUA = new CountryIsoCode("Nicaragua", "NI", "NIC", 558);

    public static final CountryIsoCode NIGER = new CountryIsoCode("Niger", "NE", "NER", 562);

    public static final CountryIsoCode NIGERIA = new CountryIsoCode("Nigeria", "NG", "NGA", 566);

    public static final CountryIsoCode NIUE = new CountryIsoCode("Niue", "NU", "NIU", 570);

    public static final CountryIsoCode NORFOLK_ISLAND = new CountryIsoCode("Norfolk Island", "NF", "NFK", 574);

    public static final CountryIsoCode NORTHERN_MARIANA_ISLANDS = new CountryIsoCode("Northern Mariana Islands", "MP", "MNP", 580);

    public static final CountryIsoCode NORWAY = new CountryIsoCode("Norway", "NO", "NOR", 578);

    public static final CountryIsoCode OMAN = new CountryIsoCode("Oman", "OM", "OMN", 512);

    public static final CountryIsoCode PAKISTAN = new CountryIsoCode("Pakistan", "PK", "PAK", 586);

    public static final CountryIsoCode PALAU = new CountryIsoCode("Palau", "PW", "PLW", 585);

    public static final CountryIsoCode PALESTINE = new CountryIsoCode("Palestine", "PS", "PSE", 275);

    public static final CountryIsoCode PANAMA = new CountryIsoCode("Panama", "PA", "PAN", 591);

    public static final CountryIsoCode PAPUA_NEW_GUINEA = new CountryIsoCode("Papua New Guinea", "PG", "PNG", 598);

    public static final CountryIsoCode PARAGUAY = new CountryIsoCode("Paraguay", "PY", "PRY", 600);

    public static final CountryIsoCode PERU = new CountryIsoCode("Peru", "PE", "PER", 604);

    public static final CountryIsoCode PHILIPPINES = new CountryIsoCode("Philippines", "PH", "PHL", 608);

    public static final CountryIsoCode PITCAIRN = new CountryIsoCode("Pitcairn", "PN", "PCN", 612);

    public static final CountryIsoCode POLAND = new CountryIsoCode("Poland", "PL", "POL", 616);

    public static final CountryIsoCode PORTUGAL = new CountryIsoCode("Portugal", "PT", "PRT", 620);

    public static final CountryIsoCode PUERTO_RICO = new CountryIsoCode("Puerto Rico", "PR", "PRI", 630);

    public static final CountryIsoCode QATAR = new CountryIsoCode("Qatar", "QA", "QAT", 634);

    public static final CountryIsoCode REUNION = new CountryIsoCode("Reunion", "RE", "REU", 638);

    public static final CountryIsoCode ROMANIA = new CountryIsoCode("Romania", "RO", "ROU", 642);

    public static final CountryIsoCode RUSSIA = new CountryIsoCode("Russia", "RU", "RUS", 643);

    public static final CountryIsoCode RWANDA = new CountryIsoCode("Rwanda", "RW", "RWA", 646);

    public static final CountryIsoCode SAINT_BARTHELEMY = new CountryIsoCode("Saint Barthelemy", "BL", "BLM", 652);

    public static final CountryIsoCode SAINT_HELENA = new CountryIsoCode("Saint Helena", "SH", "SHN", 654);

    public static final CountryIsoCode SAINT_KITTS_AND_NEVIS = new CountryIsoCode("Saint Kitts and Nevis", "KN", "KNA", 659);

    public static final CountryIsoCode SAINT_LUCIA = new CountryIsoCode("Saint Lucia", "LC", "LCA", 662);

    public static final CountryIsoCode SAINT_MARTIN_FRENCH_PART = new CountryIsoCode("Saint Martin - French Part", "MF", "MAF", 663);

    public static final CountryIsoCode SAINT_PIERRE_AND_MIQUELON = new CountryIsoCode("Saint Pierre and Miquelon", "PM", "SPM", 666);

    public static final CountryIsoCode SAINT_VINCENT_AND_THE_GRENADINES = new CountryIsoCode("Saint Vincent and the Grenadines", "VC", "VCT", 670);

    public static final CountryIsoCode SAMOA = new CountryIsoCode("Samoa", "WS", "WSM", 882);

    public static final CountryIsoCode SAN_MARINO = new CountryIsoCode("San Marino", "SM", "SMR", 674);

    public static final CountryIsoCode SAO_TOME_AND_PRINCIPE = new CountryIsoCode("Sao Tome And Principe", "ST", "STP", 678);

    public static final CountryIsoCode SAUDI_ARABIA = new CountryIsoCode("Saudi Arabia", "SA", "SAU", 682);

    public static final CountryIsoCode SENEGAL = new CountryIsoCode("Senegal", "SN", "SEN", 686);

    public static final CountryIsoCode SERBIA = new CountryIsoCode("Serbia", "RS", "SRB", 688);

    public static final CountryIsoCode SEYCHELLES = new CountryIsoCode("Seychelles", "SC", "SYC", 690);

    public static final CountryIsoCode SIERRA_LEONE = new CountryIsoCode("Sierra Leone", "SL", "SLE", 694);

    public static final CountryIsoCode SINGAPORE = new CountryIsoCode("Singapore", "SG", "SGP", 702);

    public static final CountryIsoCode SLOVAKIA = new CountryIsoCode("Slovakia", "SK", "SVK", 703);

    public static final CountryIsoCode SLOVENIA = new CountryIsoCode("Slovenia", "SI", "SVN", 705);

    public static final CountryIsoCode SOLOMON_ISLANDS = new CountryIsoCode("Solomon Islands", "SB", "SLB", 90);

    public static final CountryIsoCode SOMALIA = new CountryIsoCode("Somalia", "SO", "SOM", 706);

    public static final CountryIsoCode SOUTH_AFRICA = new CountryIsoCode("South Africa", "ZA", "ZAF", 710);

    public static final CountryIsoCode SOUTH_GEORGIA_AND_SOUTH_SANDWICH_ISLANDS = new CountryIsoCode("South Georgia and South Sandwich Islands", "GS", "SGS", 239);

    public static final CountryIsoCode SOUTH_SUDAN = new CountryIsoCode("South Sudan", "SS", "SSD", 728);

    public static final CountryIsoCode SPAIN = new CountryIsoCode("Spain", "ES", "ESP", 724);

    public static final CountryIsoCode SRI_LANKA = new CountryIsoCode("Sri Lanka", "LK", "LKA", 144);

    public static final CountryIsoCode SUDAN = new CountryIsoCode("Sudan", "SD", "SDN", 729);

    public static final CountryIsoCode SURINAME = new CountryIsoCode("Suriname", "SR", "SUR", 740);

    public static final CountryIsoCode SVALBARD_AND_JAN_MAYEN_ISLANDS = new CountryIsoCode("Svalbard and Jan Mayen Islands", "SJ", "SJM", 744);

    public static final CountryIsoCode SWAZILAND = new CountryIsoCode("Swaziland", "SZ", "SWZ", 748);

    public static final CountryIsoCode SWEDEN = new CountryIsoCode("Sweden", "SE", "SWE", 752);

    public static final CountryIsoCode SWITZERLAND = new CountryIsoCode("Switzerland", "CH", "CHE", 756);

    public static final CountryIsoCode SYRIA = new CountryIsoCode("Syria", "SY", "SYR", 760);

    public static final CountryIsoCode TAIWAN = new CountryIsoCode("Taiwan", "TW", "TWN", 158);

    public static final CountryIsoCode TAJIKISTAN = new CountryIsoCode("Tajikistan", "TJ", "TJK", 762);

    public static final CountryIsoCode TANZANIA = new CountryIsoCode("Tanzania", "TZ", "TZA", 834);

    public static final CountryIsoCode THAILAND = new CountryIsoCode("Thailand", "TH", "THA", 764);

    public static final CountryIsoCode TIMOR_LESTE = new CountryIsoCode("Timor Leste", "TL", "TLS", 626);

    public static final CountryIsoCode TOGO = new CountryIsoCode("Togo", "TG", "TGO", 768);

    public static final CountryIsoCode TOKELAU = new CountryIsoCode("Tokelau", "TK", "TKL", 772);

    public static final CountryIsoCode TONGA = new CountryIsoCode("Tonga", "TO", "TON", 776);

    public static final CountryIsoCode TRINIDAD_AND_TOBAGO = new CountryIsoCode("Trinidad And Tobago", "TT", "TTO", 780);

    public static final CountryIsoCode TUNISIA = new CountryIsoCode("Tunisia", "TN", "TUN", 788);

    public static final CountryIsoCode TURKEY = new CountryIsoCode("Turkey", "TR", "TUR", 792);

    public static final CountryIsoCode TURKMENISTAN = new CountryIsoCode("Turkmenistan", "TM", "TKM", 795);

    public static final CountryIsoCode TURKS_AND_CAICOS_ISLANDS = new CountryIsoCode("Turks and Caicos Islands", "TC", "TCA", 796);

    public static final CountryIsoCode TUVALU = new CountryIsoCode("Tuvalu", "TV", "TUV", 798);

    public static final CountryIsoCode UGANDA = new CountryIsoCode("UGANDA", "UG", "UGA", 800);

    public static final CountryIsoCode UKRAINE = new CountryIsoCode("Ukraine", "UA", "UKR", 804);

    public static final CountryIsoCode UNITED_ARAB_EMIRATES = new CountryIsoCode("United Arab Emirates", "AE", "ARE", 784);

    public static final CountryIsoCode UNITED_KINGDOM = new CountryIsoCode("United Kingdom", "GB", "GBR", 826);

    public static final CountryIsoCode UNITED_STATES = new CountryIsoCode("United States", "US", "USA", 840);

    public static final CountryIsoCode UNITED_STATES_MINOR_OUTLYING_ISLANDS = new CountryIsoCode("United States Minor Outlying Islands", "UM", "UMI", 581);

    public static final CountryIsoCode URUGUAY = new CountryIsoCode("Uruguay", "UY", "URY", 858);

    public static final CountryIsoCode UZBEKISTAN = new CountryIsoCode("Uzbekistan", "UZ", "UZB", 860);

    public static final CountryIsoCode VANUATU = new CountryIsoCode("Vanuatu", "VU", "VUT", 548);

    public static final CountryIsoCode VATICAN = new CountryIsoCode("Vatican", "VA", "VAT", 336);

    public static final CountryIsoCode VENEZUELA = new CountryIsoCode("Venezuela", "VE", "VEN", 862);

    public static final CountryIsoCode VIETNAM = new CountryIsoCode("Vietnam", "VN", "VNM", 704);

    public static final CountryIsoCode VIRGIN_ISLANDS = new CountryIsoCode("Virgin Islands", "VI", "VIR", 850);

    public static final CountryIsoCode VIRGIN_ISLANDS_BRITISH = new CountryIsoCode("Virgin Islands British", "VG", "VGB", 92);

    public static final CountryIsoCode WALLIS_AND_FUTUNA_ISLANDS = new CountryIsoCode("Wallis And Futuna Islands", "WF", "WLF", 876);

    public static final CountryIsoCode WESTERN_SAHARA = new CountryIsoCode("Western Sahara", "EH", "ESH", 732);

    public static final CountryIsoCode YEMEN = new CountryIsoCode("Yemen", "YE", "YEM", 887);

    public static final CountryIsoCode ZAMBIA = new CountryIsoCode("Zambia", "ZM", "ZMB", 894);

    public static final CountryIsoCode ZIMBABWE = new CountryIsoCode("Zimbabwe", "ZW", "ZWE", 716);

    /** The name of this country */
    private final String name;

    /** ISO alpha2 country code (2 letters) */
    private final String alpha2CountryCode;

    /** ISO alpha3 country code (3 letters) */
    private final String alpha3CountryCode;

    /** ISO numeric country code (3 digit) */
    private final int numericCountryCode;

    public CountryIsoCode(final String name, final String alpha2CountryCode, final String alpha3CountryCode,
                          final int numericCountryCode)
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
    @Override
    public boolean equals(final Object object)
    {
        if (object instanceof CountryIsoCode)
        {
            final var that = (CountryIsoCode) object;
            return alpha2CountryCode.equals(that.alpha2CountryCode);
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        return alpha2CountryCode.hashCode();
    }

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

    @Override
    public String toString()
    {
        return alpha2CountryCode;
    }
}
