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

import static com.telenav.kivakit.annotations.code.ApiStability.STABLE_EXPANDABLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.FULLY_DOCUMENTED;
import static com.telenav.kivakit.annotations.code.TestingQuality.UNTESTED;

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
@SuppressWarnings("JavadocLinkAsPlainText")
@ApiQuality(stability = STABLE_EXPANDABLE,
            testing = UNTESTED,
            documentation = FULLY_DOCUMENTED)
public class LocaleCountry
{
    public static final LocaleCountry AFGHANISTAN = new LocaleCountry("Afghanistan", "AF", "AFG", 4);

    public static final LocaleCountry ALAND = new LocaleCountry("Aland", "AX", "ALA", 248);

    public static final LocaleCountry ALBANIA = new LocaleCountry("Albania", "AL", "ALB", 8);

    public static final LocaleCountry ALGERIA = new LocaleCountry("Algeria", "DZ", "DZA", 12);

    public static final LocaleCountry AMERICAN_SAMOA = new LocaleCountry("American Samoa", "AS", "ASM", 16);

    public static final LocaleCountry ANDORRA = new LocaleCountry("Andorra", "AD", "AND", 20);

    public static final LocaleCountry ANGOLA = new LocaleCountry("Angola", "AO", "AGO", 24);

    public static final LocaleCountry ANGUILLA = new LocaleCountry("Anguilla", "AI", "AIA", 660);

    public static final LocaleCountry ANTIGUA_AND_BARBUDA = new LocaleCountry("Antigua And Barbuda", "AG", "ATG", 28);

    public static final LocaleCountry ARGENTINA = new LocaleCountry("Argentina", "AR", "ARG", 32);

    public static final LocaleCountry ARMENIA = new LocaleCountry("Armenia", "AM", "ARM", 51);

    public static final LocaleCountry ARUBA = new LocaleCountry("Aruba", "AW", "ABW", 533);

    public static final LocaleCountry AUSTRALIA = new LocaleCountry("Australia", "AU", "AUS", 36);

    public static final LocaleCountry AUSTRIA = new LocaleCountry("Austria", "AT", "AUT", 40);

    public static final LocaleCountry AZERBAIJAN = new LocaleCountry("Azerbaijan", "AZ", "AZE", 31);

    public static final LocaleCountry BAHAMAS = new LocaleCountry("Bahamas", "BS", "BHS", 44);

    public static final LocaleCountry BAHRAIN = new LocaleCountry("Bahrain", "BH", "BHR", 48);

    public static final LocaleCountry BANGLADESH = new LocaleCountry("Bangladesh", "BD", "BGD", 50);

    public static final LocaleCountry BARBADOS = new LocaleCountry("Barbados", "BB", "BRB", 52);

    public static final LocaleCountry BELARUS = new LocaleCountry("Belarus", "BY", "BLR", 112);

    public static final LocaleCountry BELGIUM = new LocaleCountry("Belgium", "BE", "BEL", 56);

    public static final LocaleCountry BELIZE = new LocaleCountry("Belize", "BZ", "BLZ", 84);

    public static final LocaleCountry BENIN = new LocaleCountry("Benin", "BJ", "BEN", 204);

    public static final LocaleCountry BERMUDA = new LocaleCountry("Bermuda", "BM", "BMU", 60);

    public static final LocaleCountry BHUTAN = new LocaleCountry("Bhutan", "BT", "BTN", 64);

    public static final LocaleCountry BOLIVIA = new LocaleCountry("Bolivia", "BO", "BOL", 68);

    public static final LocaleCountry BOSNIA_AND_HERZEGOVINA = new LocaleCountry("Bosnia And Herzegovina", "BA", "BIH", 70);

    public static final LocaleCountry BOTSWANA = new LocaleCountry("Botswana", "BW", "BWA", 72);

    public static final LocaleCountry BRAZIL = new LocaleCountry("Brazil", "BR", "BRA", 76);

    public static final LocaleCountry BRITISH_INDIAN_OCEAN_TERRITORY = new LocaleCountry("British Indian Ocean Territory", "IO", "IOT", 86);

    @SuppressWarnings("SpellCheckingInspection")
    public static final LocaleCountry BRUNEI_DARUSSALAM = new LocaleCountry("Brunei Darussalam", "BN", "BRN", 96);

    public static final LocaleCountry BULGARIA = new LocaleCountry("Bulgaria", "BG", "BGR", 100);

    public static final LocaleCountry BURKINA_FASO = new LocaleCountry("Burkina Faso", "BF", "BFA", 854);

    public static final LocaleCountry BURUNDI = new LocaleCountry("Burundi", "BI", "BDI", 108);

    public static final LocaleCountry CAMBODIA = new LocaleCountry("Cambodia", "KH", "KHM", 116);

    public static final LocaleCountry CAMEROON = new LocaleCountry("Cameroon", "CM", "CMR", 120);

    public static final LocaleCountry CANADA = new LocaleCountry("Canada", "CA", "CAN", 124);

    public static final LocaleCountry CAPE_VERDE = new LocaleCountry("Cape Verde", "CV", "CPV", 132);

    public static final LocaleCountry CAYMAN_ISLANDS = new LocaleCountry("Cayman Islands", "KY", "CYM", 136);

    public static final LocaleCountry CENTRAL_AFRICAN_REPUBLIC = new LocaleCountry("Central African Republic", "CF", "CAF", 140);

    public static final LocaleCountry CHAD = new LocaleCountry("Chad", "TD", "TCD", 148);

    public static final LocaleCountry CHILE = new LocaleCountry("Chile", "CL", "CHL", 152);

    public static final LocaleCountry CHINA = new LocaleCountry("China", "CN", "CHN", 156);

    public static final LocaleCountry CHRISTMAS_ISLAND = new LocaleCountry("Christmas Island", "CX", "CXR", 162);

    public static final LocaleCountry COCOS_KEELING_ISLANDS = new LocaleCountry("Cocos Keeling Islands", "CC", "CCK", 166);

    public static final LocaleCountry COLOMBIA = new LocaleCountry("Colombia", "CO", "COL", 170);

    public static final LocaleCountry COMOROS = new LocaleCountry("Comoros", "KM", "COM", 174);

    public static final LocaleCountry CONGO_BRAZZAVILLE = new LocaleCountry("Congo Brazzaville", "CG", "COG", 178);

    public static final LocaleCountry CONGO_KINSHASA = new LocaleCountry("Congo Kinshasa", "CD", "COD", 180);

    public static final LocaleCountry COOK_ISLANDS = new LocaleCountry("Cook Islands", "CK", "COK", 184);

    @SuppressWarnings("SpellCheckingInspection")
    public static final LocaleCountry COSTA_RICA = new LocaleCountry("Costa Rica", "CR", "CRI", 188);

    @SuppressWarnings("SpellCheckingInspection")
    public static final LocaleCountry COTE_D_IVOIRE = new LocaleCountry("Cote D'Ivoire", "CI", "CIV", 384);

    public static final LocaleCountry CROATIA = new LocaleCountry("Croatia", "HR", "HRV", 191);

    public static final LocaleCountry CUBA = new LocaleCountry("Cuba", "CU", "CUB", 192);

    public static final LocaleCountry CYPRUS = new LocaleCountry("Cyprus", "CY", "CYP", 196);

    public static final LocaleCountry CZECH_REPUBLIC = new LocaleCountry("Czech Republic", "CZ", "CZE", 203);

    public static final LocaleCountry DENMARK = new LocaleCountry("Denmark", "DK", "DNK", 208);

    public static final LocaleCountry DJIBOUTI = new LocaleCountry("Djibouti", "DJ", "DJI", 262);

    public static final LocaleCountry DOMINICA = new LocaleCountry("Dominica", "DM", "DMA", 212);

    public static final LocaleCountry DOMINICAN_REPUBLIC = new LocaleCountry("Dominican Republic", "DO", "DOM", 214);

    public static final LocaleCountry ECUADOR = new LocaleCountry("Ecuador", "EC", "ECU", 218);

    public static final LocaleCountry EGYPT = new LocaleCountry("Egypt", "EG", "EGY", 818);

    public static final LocaleCountry EL_SALVADOR = new LocaleCountry("El Salvador", "SV", "SLV", 222);

    public static final LocaleCountry EQUATORIAL_GUINEA = new LocaleCountry("Equatorial Guinea", "GQ", "GNQ", 226);

    public static final LocaleCountry ERITREA = new LocaleCountry("Eritrea", "ER", "ERI", 232);

    public static final LocaleCountry ESTONIA = new LocaleCountry("Estonia", "EE", "EST", 233);

    public static final LocaleCountry ETHIOPIA = new LocaleCountry("Ethiopia", "ET", "ETH", 231);

    public static final LocaleCountry FALKLAND_ISLANDS = new LocaleCountry("Falkland Islands", "FK", "FLK", 260);

    @SuppressWarnings("SpellCheckingInspection")
    public static final LocaleCountry FAROE_ISLANDS = new LocaleCountry("Faroe Islands", "FO", "FRO", 234);

    public static final LocaleCountry FIJI = new LocaleCountry("Fiji", "FJ", "FJI", 242);

    public static final LocaleCountry FINLAND = new LocaleCountry("Finland", "FI", "FIN", 246);

    public static final LocaleCountry FRANCE = new LocaleCountry("France", "FR", "FRA", 250);

    public static final LocaleCountry FRENCH_GUIANA = new LocaleCountry("French Guiana", "GF", "GUF", 254);

    public static final LocaleCountry FRENCH_POLYNESIA = new LocaleCountry("French Polynesia", "PF", "PYF", 258);

    public static final LocaleCountry FRENCH_SOUTHERN_AND_ANTARCTIC_LANDS = new LocaleCountry("French Southern and Antarctic Lands", "TF", "ATF", 238);

    public static final LocaleCountry GABON = new LocaleCountry("Gabon", "GA", "GAB", 266);

    public static final LocaleCountry GAMBIA = new LocaleCountry("Gambia", "GM", "GMB", 270);

    public static final LocaleCountry GEORGIA = new LocaleCountry("Georgia", "GE", "GEO", 268);

    public static final LocaleCountry GERMANY = new LocaleCountry("Germany", "DE", "DEU", 276);

    public static final LocaleCountry GHANA = new LocaleCountry("Ghana", "GH", "GHA", 288);

    public static final LocaleCountry GIBRALTAR = new LocaleCountry("Gibraltar", "GI", "GIB", 292);

    public static final LocaleCountry GREECE = new LocaleCountry("Greece", "GR", "GRC", 300);

    public static final LocaleCountry GREENLAND = new LocaleCountry("Greenland", "GL", "GRL", 304);

    public static final LocaleCountry GRENADA = new LocaleCountry("Grenada", "GD", "GRD", 308);

    public static final LocaleCountry GUADELOUPE = new LocaleCountry("Guadeloupe", "GP", "GLP", 312);

    public static final LocaleCountry GUAM = new LocaleCountry("Guam", "GU", "GUM", 316);

    public static final LocaleCountry GUATEMALA = new LocaleCountry("Guatemala", "GT", "GTM", 320);

    public static final LocaleCountry GUERNSEY = new LocaleCountry("Guernsey", "GG", "GGY", 831);

    public static final LocaleCountry GUINEA = new LocaleCountry("Guinea", "GN", "GIN", 324);

    public static final LocaleCountry GUINEA_BISSAU = new LocaleCountry("Guinea Bissau", "GW", "GNB", 624);

    public static final LocaleCountry GUYANA = new LocaleCountry("Guyana", "GY", "GUY", 328);

    public static final LocaleCountry HAITI = new LocaleCountry("Haiti", "HT", "HTI", 332);

    public static final LocaleCountry HONDURAS = new LocaleCountry("Honduras", "HN", "HND", 340);

    public static final LocaleCountry HONG_KONG = new LocaleCountry("Hong Kong", "HK", "HKG", 344);

    public static final LocaleCountry HUNGARY = new LocaleCountry("Hungary", "HU", "HUN", 348);

    public static final LocaleCountry ICELAND = new LocaleCountry("Iceland", "IS", "ISL", 352);

    public static final LocaleCountry INDIA = new LocaleCountry("India", "IN", "IND", 356);

    public static final LocaleCountry INDONESIA = new LocaleCountry("Indonesia", "ID", "IDN", 360);

    public static final LocaleCountry IRAN = new LocaleCountry("Iran", "IR", "IRN", 364);

    public static final LocaleCountry IRAQ = new LocaleCountry("Iraq", "IQ", "IRQ", 368);

    public static final LocaleCountry IRELAND = new LocaleCountry("Ireland", "IE", "IRL", 372);

    public static final LocaleCountry ISLE_OF_MAN = new LocaleCountry("Isle of Man", "IM", "IMN", 833);

    public static final LocaleCountry ISRAEL = new LocaleCountry("Israel", "IL", "ISR", 376);

    public static final LocaleCountry ITALY = new LocaleCountry("Italy", "IT", "ITA", 380);

    public static final LocaleCountry JAMAICA = new LocaleCountry("Jamaica", "JM", "JAM", 388);

    public static final LocaleCountry JAPAN = new LocaleCountry("Japan", "JP", "JPN", 392);

    public static final LocaleCountry JERSEY = new LocaleCountry("Jersey", "JE", "JEY", 832);

    public static final LocaleCountry JORDAN = new LocaleCountry("Jordan", "JO", "JOR", 400);

    public static final LocaleCountry KAZAKHSTAN = new LocaleCountry("Kazakhstan", "KZ", "KAZ", 398);

    public static final LocaleCountry KENYA = new LocaleCountry("Kenya", "KE", "KEN", 404);

    public static final LocaleCountry KIRIBATI = new LocaleCountry("Kiribati", "KI", "KIR", 296);

    public static final LocaleCountry KOREA_NORTH = new LocaleCountry("Korea North", "KP", "PRK", 408);

    public static final LocaleCountry KOREA_SOUTH = new LocaleCountry("Korea South", "KR", "KOR", 410);

    public static final LocaleCountry KUWAIT = new LocaleCountry("Kuwait", "KW", "KWT", 414);

    public static final LocaleCountry KYRGYZSTAN = new LocaleCountry("Kyrgyzstan", "KG", "KGZ", 417);

    public static final LocaleCountry LAOS = new LocaleCountry("Laos", "LA", "LAO", 418);

    public static final LocaleCountry LATVIA = new LocaleCountry("Latvia", "LV", "LVA", 428);

    public static final LocaleCountry LEBANON = new LocaleCountry("Lebanon", "LB", "LBN", 422);

    public static final LocaleCountry LESOTHO = new LocaleCountry("Lesotho", "LS", "LSO", 426);

    public static final LocaleCountry LIBERIA = new LocaleCountry("Liberia", "LR", "LBR", 430);

    public static final LocaleCountry LIBYA = new LocaleCountry("Libya", "LY", "LBY", 434);

    public static final LocaleCountry LIECHTENSTEIN = new LocaleCountry("Liechtenstein", "LI", "LIE", 438);

    public static final LocaleCountry LITHUANIA = new LocaleCountry("Lithuania", "LT", "LTU", 440);

    public static final LocaleCountry LUXEMBOURG = new LocaleCountry("Luxembourg", "LU", "LUX", 442);

    public static final LocaleCountry MACAU = new LocaleCountry("Macau", "MO", "MAC", 446);

    public static final LocaleCountry MACEDONIA = new LocaleCountry("Macedonia", "MK", "MKD", 807);

    public static final LocaleCountry MADAGASCAR = new LocaleCountry("Madagascar", "MG", "MDG", 450);

    public static final LocaleCountry MALAWI = new LocaleCountry("Malawi", "MW", "MWI", 454);

    public static final LocaleCountry MALAYSIA = new LocaleCountry("Malaysia", "MY", "MYS", 458);

    public static final LocaleCountry MALDIVES = new LocaleCountry("Maldives", "MV", "MDV", 462);

    public static final LocaleCountry MALI = new LocaleCountry("Mali", "ML", "MLI", 466);

    public static final LocaleCountry MALTA = new LocaleCountry("Malta", "MT", "MLT", 470);

    public static final LocaleCountry MARSHALL_ISLANDS = new LocaleCountry("Marshall Islands", "MH", "MHL", 584);

    public static final LocaleCountry MARTINIQUE = new LocaleCountry("Martinique", "MQ", "MTQ", 474);

    public static final LocaleCountry MAURITANIA = new LocaleCountry("Mauritania", "MR", "MRT", 478);

    public static final LocaleCountry MAURITIUS = new LocaleCountry("Mauritius", "MU", "MUS", 480);

    @SuppressWarnings("SpellCheckingInspection")
    public static final LocaleCountry MAYOTTE = new LocaleCountry("Mayotte", "YT", "MYT", 175);

    public static final LocaleCountry MEXICO = new LocaleCountry("Mexico", "MX", "MEX", 484);

    public static final LocaleCountry MICRONESIA = new LocaleCountry("Micronesia", "FM", "FSM", 583);

    public static final LocaleCountry MOLDOVA = new LocaleCountry("Moldova", "MD", "MDA", 498);

    public static final LocaleCountry MONACO = new LocaleCountry("Monaco", "MC", "MCO", 492);

    public static final LocaleCountry MONGOLIA = new LocaleCountry("Mongolia", "MN", "MNG", 496);

    public static final LocaleCountry MONTENEGRO = new LocaleCountry("Montenegro", "ME", "MNE", 499);

    public static final LocaleCountry MONTSERRAT = new LocaleCountry("Montserrat", "MS", "MSR", 500);

    public static final LocaleCountry MOROCCO = new LocaleCountry("Morocco", "MA", "MAR", 504);

    public static final LocaleCountry MOZAMBIQUE = new LocaleCountry("Mozambique", "MZ", "MOZ", 508);

    public static final LocaleCountry MYANMAR = new LocaleCountry("Myanmar", "MM", "MMR", 104);

    public static final LocaleCountry NAMIBIA = new LocaleCountry("Namibia", "NA", "NAM", 516);

    public static final LocaleCountry NAURU = new LocaleCountry("Nauru", "NR", "NRU", 520);

    public static final LocaleCountry NEPAL = new LocaleCountry("Nepal", "NP", "NPL", 524);

    public static final LocaleCountry NETHERLANDS = new LocaleCountry("Netherlands", "NL", "NLD", 528);

    public static final LocaleCountry NETHERLANDS_ANTILLES = new LocaleCountry("Netherlands Antilles", "AN", "ANT", 530);

    public static final LocaleCountry NEW_CALEDONIA = new LocaleCountry("New Caledonia", "NC", "NCL", 540);

    public static final LocaleCountry NEW_ZEALAND = new LocaleCountry("New Zealand", "NZ", "NZL", 554);

    public static final LocaleCountry NICARAGUA = new LocaleCountry("Nicaragua", "NI", "NIC", 558);

    public static final LocaleCountry NIGER = new LocaleCountry("Niger", "NE", "NER", 562);

    public static final LocaleCountry NIGERIA = new LocaleCountry("Nigeria", "NG", "NGA", 566);

    @SuppressWarnings("SpellCheckingInspection")
    public static final LocaleCountry NIUE = new LocaleCountry("Niue", "NU", "NIU", 570);

    public static final LocaleCountry NORFOLK_ISLAND = new LocaleCountry("Norfolk Island", "NF", "NFK", 574);

    public static final LocaleCountry NORTHERN_MARIANA_ISLANDS = new LocaleCountry("Northern Mariana Islands", "MP", "MNP", 580);

    public static final LocaleCountry NORWAY = new LocaleCountry("Norway", "NO", "NOR", 578);

    public static final LocaleCountry OMAN = new LocaleCountry("Oman", "OM", "OMN", 512);

    public static final LocaleCountry PAKISTAN = new LocaleCountry("Pakistan", "PK", "PAK", 586);

    @SuppressWarnings("SpellCheckingInspection")
    public static final LocaleCountry PALAU = new LocaleCountry("Palau", "PW", "PLW", 585);

    public static final LocaleCountry PALESTINE = new LocaleCountry("Palestine", "PS", "PSE", 275);

    public static final LocaleCountry PANAMA = new LocaleCountry("Panama", "PA", "PAN", 591);

    public static final LocaleCountry PAPUA_NEW_GUINEA = new LocaleCountry("Papua New Guinea", "PG", "PNG", 598);

    public static final LocaleCountry PARAGUAY = new LocaleCountry("Paraguay", "PY", "PRY", 600);

    public static final LocaleCountry PERU = new LocaleCountry("Peru", "PE", "PER", 604);

    public static final LocaleCountry PHILIPPINES = new LocaleCountry("Philippines", "PH", "PHL", 608);

    public static final LocaleCountry PITCAIRN = new LocaleCountry("Pitcairn", "PN", "PCN", 612);

    public static final LocaleCountry POLAND = new LocaleCountry("Poland", "PL", "POL", 616);

    public static final LocaleCountry PORTUGAL = new LocaleCountry("Portugal", "PT", "PRT", 620);

    public static final LocaleCountry PUERTO_RICO = new LocaleCountry("Puerto Rico", "PR", "PRI", 630);

    public static final LocaleCountry QATAR = new LocaleCountry("Qatar", "QA", "QAT", 634);

    public static final LocaleCountry REUNION = new LocaleCountry("Reunion", "RE", "REU", 638);

    public static final LocaleCountry ROMANIA = new LocaleCountry("Romania", "RO", "ROU", 642);

    public static final LocaleCountry RUSSIA = new LocaleCountry("Russia", "RU", "RUS", 643);

    public static final LocaleCountry RWANDA = new LocaleCountry("Rwanda", "RW", "RWA", 646);

    @SuppressWarnings("SpellCheckingInspection")
    public static final LocaleCountry SAINT_BARTHELEMY = new LocaleCountry("Saint Barthelemy", "BL", "BLM", 652);

    public static final LocaleCountry SAINT_HELENA = new LocaleCountry("Saint Helena", "SH", "SHN", 654);

    @SuppressWarnings("SpellCheckingInspection")
    public static final LocaleCountry SAINT_KITTS_AND_NEVIS = new LocaleCountry("Saint Kitts and Nevis", "KN", "KNA", 659);

    public static final LocaleCountry SAINT_LUCIA = new LocaleCountry("Saint Lucia", "LC", "LCA", 662);

    public static final LocaleCountry SAINT_MARTIN_FRENCH_PART = new LocaleCountry("Saint Martin - French Part", "MF", "MAF", 663);

    public static final LocaleCountry SAINT_PIERRE_AND_MIQUELON = new LocaleCountry("Saint Pierre and Miquelon", "PM", "SPM", 666);

    public static final LocaleCountry SAINT_VINCENT_AND_THE_GRENADINES = new LocaleCountry("Saint Vincent and the Grenadines", "VC", "VCT", 670);

    public static final LocaleCountry SAMOA = new LocaleCountry("Samoa", "WS", "WSM", 882);

    public static final LocaleCountry SAN_MARINO = new LocaleCountry("San Marino", "SM", "SMR", 674);

    public static final LocaleCountry SAO_TOME_AND_PRINCIPE = new LocaleCountry("Sao Tome And Principe", "ST", "STP", 678);

    public static final LocaleCountry SAUDI_ARABIA = new LocaleCountry("Saudi Arabia", "SA", "SAU", 682);

    public static final LocaleCountry SENEGAL = new LocaleCountry("Senegal", "SN", "SEN", 686);

    public static final LocaleCountry SERBIA = new LocaleCountry("Serbia", "RS", "SRB", 688);

    public static final LocaleCountry SEYCHELLES = new LocaleCountry("Seychelles", "SC", "SYC", 690);

    public static final LocaleCountry SIERRA_LEONE = new LocaleCountry("Sierra Leone", "SL", "SLE", 694);

    public static final LocaleCountry SINGAPORE = new LocaleCountry("Singapore", "SG", "SGP", 702);

    public static final LocaleCountry SLOVAKIA = new LocaleCountry("Slovakia", "SK", "SVK", 703);

    public static final LocaleCountry SLOVENIA = new LocaleCountry("Slovenia", "SI", "SVN", 705);

    public static final LocaleCountry SOLOMON_ISLANDS = new LocaleCountry("Solomon Islands", "SB", "SLB", 90);

    public static final LocaleCountry SOMALIA = new LocaleCountry("Somalia", "SO", "SOM", 706);

    public static final LocaleCountry SOUTH_AFRICA = new LocaleCountry("South Africa", "ZA", "ZAF", 710);

    public static final LocaleCountry SOUTH_GEORGIA_AND_SOUTH_SANDWICH_ISLANDS = new LocaleCountry("South Georgia and South Sandwich Islands", "GS", "SGS", 239);

    public static final LocaleCountry SOUTH_SUDAN = new LocaleCountry("South Sudan", "SS", "SSD", 728);

    public static final LocaleCountry SPAIN = new LocaleCountry("Spain", "ES", "ESP", 724);

    public static final LocaleCountry SRI_LANKA = new LocaleCountry("Sri Lanka", "LK", "LKA", 144);

    public static final LocaleCountry SUDAN = new LocaleCountry("Sudan", "SD", "SDN", 729);

    public static final LocaleCountry SURINAME = new LocaleCountry("Suriname", "SR", "SUR", 740);

    @SuppressWarnings("SpellCheckingInspection")
    public static final LocaleCountry SVALBARD_AND_JAN_MAYEN_ISLANDS = new LocaleCountry("Svalbard and Jan Mayen Islands", "SJ", "SJM", 744);

    public static final LocaleCountry SWAZILAND = new LocaleCountry("Swaziland", "SZ", "SWZ", 748);

    public static final LocaleCountry SWEDEN = new LocaleCountry("Sweden", "SE", "SWE", 752);

    public static final LocaleCountry SWITZERLAND = new LocaleCountry("Switzerland", "CH", "CHE", 756);

    public static final LocaleCountry SYRIA = new LocaleCountry("Syria", "SY", "SYR", 760);

    public static final LocaleCountry TAIWAN = new LocaleCountry("Taiwan", "TW", "TWN", 158);

    public static final LocaleCountry TAJIKISTAN = new LocaleCountry("Tajikistan", "TJ", "TJK", 762);

    public static final LocaleCountry TANZANIA = new LocaleCountry("Tanzania", "TZ", "TZA", 834);

    public static final LocaleCountry THAILAND = new LocaleCountry("Thailand", "TH", "THA", 764);

    public static final LocaleCountry TIMOR_LESTE = new LocaleCountry("Timor Leste", "TL", "TLS", 626);

    public static final LocaleCountry TOGO = new LocaleCountry("Togo", "TG", "TGO", 768);

    @SuppressWarnings("SpellCheckingInspection")
    public static final LocaleCountry TOKELAU = new LocaleCountry("Tokelau", "TK", "TKL", 772);

    public static final LocaleCountry TONGA = new LocaleCountry("Tonga", "TO", "TON", 776);

    public static final LocaleCountry TRINIDAD_AND_TOBAGO = new LocaleCountry("Trinidad And Tobago", "TT", "TTO", 780);

    public static final LocaleCountry TUNISIA = new LocaleCountry("Tunisia", "TN", "TUN", 788);

    public static final LocaleCountry TURKEY = new LocaleCountry("Turkey", "TR", "TUR", 792);

    public static final LocaleCountry TURKMENISTAN = new LocaleCountry("Turkmenistan", "TM", "TKM", 795);

    @SuppressWarnings("SpellCheckingInspection")
    public static final LocaleCountry TURKS_AND_CAICOS_ISLANDS = new LocaleCountry("Turks and Caicos Islands", "TC", "TCA", 796);

    public static final LocaleCountry TUVALU = new LocaleCountry("Tuvalu", "TV", "TUV", 798);

    public static final LocaleCountry UGANDA = new LocaleCountry("UGANDA", "UG", "UGA", 800);

    public static final LocaleCountry UKRAINE = new LocaleCountry("Ukraine", "UA", "UKR", 804);

    public static final LocaleCountry UNITED_ARAB_EMIRATES = new LocaleCountry("United Arab Emirates", "AE", "ARE", 784);

    public static final LocaleCountry UNITED_KINGDOM = new LocaleCountry("United Kingdom", "GB", "GBR", 826);

    public static final LocaleCountry UNITED_STATES = new LocaleCountry("United States", "US", "USA", 840);

    public static final LocaleCountry UNITED_STATES_MINOR_OUTLYING_ISLANDS = new LocaleCountry("United States Minor Outlying Islands", "UM", "UMI", 581);

    public static final LocaleCountry URUGUAY = new LocaleCountry("Uruguay", "UY", "URY", 858);

    public static final LocaleCountry UZBEKISTAN = new LocaleCountry("Uzbekistan", "UZ", "UZB", 860);

    public static final LocaleCountry VANUATU = new LocaleCountry("Vanuatu", "VU", "VUT", 548);

    public static final LocaleCountry VATICAN = new LocaleCountry("Vatican", "VA", "VAT", 336);

    public static final LocaleCountry VENEZUELA = new LocaleCountry("Venezuela", "VE", "VEN", 862);

    public static final LocaleCountry VIETNAM = new LocaleCountry("Vietnam", "VN", "VNM", 704);

    public static final LocaleCountry VIRGIN_ISLANDS = new LocaleCountry("Virgin Islands", "VI", "VIR", 850);

    public static final LocaleCountry VIRGIN_ISLANDS_BRITISH = new LocaleCountry("Virgin Islands British", "VG", "VGB", 92);

    @SuppressWarnings("SpellCheckingInspection")
    public static final LocaleCountry WALLIS_AND_FUTUNA_ISLANDS = new LocaleCountry("Wallis And Futuna Islands", "WF", "WLF", 876);

    public static final LocaleCountry WESTERN_SAHARA = new LocaleCountry("Western Sahara", "EH", "ESH", 732);

    public static final LocaleCountry YEMEN = new LocaleCountry("Yemen", "YE", "YEM", 887);

    public static final LocaleCountry ZAMBIA = new LocaleCountry("Zambia", "ZM", "ZMB", 894);

    public static final LocaleCountry ZIMBABWE = new LocaleCountry("Zimbabwe", "ZW", "ZWE", 716);

    /** The name of this country */
    private final String name;

    /** ISO alpha2 country code (2 letters) */
    private final String alpha2CountryCode;

    /** ISO alpha3 country code (3 letters) */
    private final String alpha3CountryCode;

    /** ISO numeric country code (3 digit) */
    private final int numericCountryCode;

    public LocaleCountry(String name,
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
        if (object instanceof LocaleCountry)
        {
            var that = (LocaleCountry) object;
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
